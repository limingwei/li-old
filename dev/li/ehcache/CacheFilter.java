package li.ehcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import li.aop.AopChain;
import li.aop.AopFilter;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheFilter implements AopFilter {
    private static volatile ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<String, ReentrantLock>();

    private static volatile CacheManager cacheManager = CacheManager.create();

    private ReentrantLock getLock(String key) {
        ReentrantLock lock = lockMap.get(key);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        ReentrantLock previousLock = lockMap.putIfAbsent(key, lock);
        return previousLock == null ? lock : previousLock;
    }

    public void doFilter(AopChain chain) {
        String cacheName = chain.getMethod().toGenericString();
        String cacheKey = chain.getArgs().toString();
        Map<String, Object> cacheData = getElement(cacheName, cacheKey);
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
        }
    }

    public static <T> T getElement(String cacheName, Object key) {
        Element element = getOrAddCache(cacheName).get(key);
        return element != null ? (T) element.getObjectValue() : null;
    }

    public static void putElement(String cacheName, Object key, Object value) {
        getOrAddCache(cacheName).put(new Element(key, value));
    }

    private static Cache getOrAddCache(String cacheName) {
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
}