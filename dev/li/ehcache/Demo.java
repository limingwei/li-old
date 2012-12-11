package li.ehcache;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class Demo {

    /**
     * 请求路径加QueryString为key,缓存request中的每一个属性
     */
    public static void main(String[] args) {
        System.out.println("hello world");
    }

    private CacheManager cacheManager = null;

    public Cache get(String name) {
        Cache cache = cacheManager.getCache(name);
        if (cache == null) {
            synchronized (cacheManager) {
                cache = cacheManager.getCache(name);
                if (cache == null) {
                    cacheManager.addCacheIfAbsent(name);
                    cache = cacheManager.getCache(name);
                }
            }
        }
        return cache;
    }

    public void put(String name, Object key, Object value) {
        get(name).put(new Element(key, value));
    }

    public <T> T get(String cacheName, Object key) {
        Element element = get(cacheName).get(key);
        return element != null ? (T) element.getObjectValue() : null;
    }

    public List getKeys(String cacheName) {
        return get(cacheName).getKeys();
    }

    public void remove(String cacheName, Object key) {
        get(cacheName).remove(key);
    }

    public void removeAll(String cacheName) {
        get(cacheName).removeAll();
    }
}