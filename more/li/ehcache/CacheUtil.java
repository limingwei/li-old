package li.ehcache;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import li.util.ConvertUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheUtil {
    /**
     * lockMap
     */
    private static volatile ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<String, ReentrantLock>();

    /**
     * CacheManager
     */
    private static volatile CacheManager cacheManager = CacheManager.create();

    /**
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
     * @param cacheName
     * @param key
     */
    public static <T> T getElement(String cacheName, Object key) {
        Element element = getOrAddCache(cacheName).get(key);
        return element != null ? (T) element.getObjectValue() : null;
    }

    /**
     * @param cacheName
     * @param key
     * @param value
     */
    public static void putElement(String cacheName, Object key, Object value) {
        getOrAddCache(cacheName).put(new Element(key, value));
    }

    /**
     * @param cacheName
     * @param key
     */
    public static void removeElement(String cacheName, Object key) {
        getOrAddCache(cacheName).remove(key);
    }

    /**
     * removeCache
     */
    public static void removeCache(String cacheName) {
        getOrAddCache(cacheName).removeAll();
    }

    /**
     * @param method
     */
    public static String cacheName(Method method) {
        li.ehcache.Cache cache = method.getAnnotation(li.ehcache.Cache.class);
        if (null != cache) {
            return cache.value();
        } else {
            return method.getDeclaringClass().getName() + "." + method.getName();
        }
    }

    /**
     * cacheKey
     */
    public static String cacheKey(Object[] args) {
        return ConvertUtil.toJson(args);
    }

    /**
     * @param key
     */
    public static ReentrantLock getLock(String key) {
        ReentrantLock lock = lockMap.get(key);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        ReentrantLock previousLock = lockMap.putIfAbsent(key, lock);
        return previousLock == null ? lock : previousLock;
    }
}