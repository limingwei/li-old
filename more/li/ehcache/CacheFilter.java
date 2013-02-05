package li.ehcache;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import li.aop.AopChain;
import li.aop.AopFilter;
import li.util.ConvertUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * CacheAopFilter
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-02-05)
 */
public class CacheFilter implements AopFilter {
    /**
     * lockMap
     */
    private static volatile ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<String, ReentrantLock>();

    /**
     * CacheManager
     */
    private static volatile CacheManager cacheManager = CacheManager.create();

    /**
     * 
     */
    public void doFilter(AopChain chain) {
        String cacheName = cacheName(chain.getMethod());
        String cacheKey = cacheKey(chain.getArgs());
        Object cacheData = getElement(cacheName, cacheKey);
        if (cacheData == null) {
            Lock lock = getLock(cacheName);
            lock.lock();
            try {
                cacheData = getElement(cacheName, cacheKey);
                if (cacheData == null) {
                    putElement(cacheName, cacheKey, chain.doFilter().getResult());
                }
            } finally {
                lock.unlock();
            }
        } else {
            chain.setResult(cacheData);
        }
    }

    /**
     * 
     * @param cacheName
     * @param key
     */
    public static <T> T getElement(String cacheName, Object key) {
        Element element = getOrAddCache(cacheName).get(key);
        return element != null ? (T) element.getObjectValue() : null;
    }

    /**
     * 
     * @param cacheName
     * @param key
     * @param value
     */
    public static void putElement(String cacheName, Object key, Object value) {
        getOrAddCache(cacheName).put(new Element(key, value));
    }

    /**
     * 
     * @param cacheName
     */
    public static Cache getOrAddCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            synchronized (cacheManager) {
                cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    cacheManager.addCacheIfAbsent(cacheName);
                    cache = cacheManager.getCache(cacheName);
                }
            }
        }
        return cache;
    }

    /**
     * 
     * @param key
     */
    private ReentrantLock getLock(String key) {
        ReentrantLock lock = lockMap.get(key);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        ReentrantLock previousLock = lockMap.putIfAbsent(key, lock);
        return previousLock == null ? lock : previousLock;
    }

    /**
     * 
     * @param method
     */
    private static String cacheName(Method method) {
        li.ehcache.Cache cache = method.getAnnotation(li.ehcache.Cache.class);
        if (null != cache) {
            return cache.value();
        } else {
            return method.getDeclaringClass().getName() + "." + method.getName();
        }
    }

    /**
     * 
     * @param method
     */
    private static String cacheKey(Object[] args) {
        return ConvertUtil.toJson(args);
    }
}