package li.ehcache;

import java.util.Map.Entry;
import java.util.Set;

import li.aop.AopChain;
import li.aop.AopFilter;
import li.mvc.Context;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheFilter implements AopFilter {
    private static CacheManager cacheManager;

    public void doFilter(AopChain chain) {

        Set<Entry<String, Object>> attributes = Context.getAttributes().entrySet();
        String cacheName = Context.getAction().toString();
        String cacheKey = Context.getRequest().getParameterMap() + Context.getRequest().getQueryString();
        for (Entry<String, Object> entry : attributes) {
            getOrAddCache(cacheName).put(new Element(entry.getKey(), entry.getValue()));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String cacheName, Object key) {
        Element element = getOrAddCache(cacheName).get(key);
        return element != null ? (T) element.getObjectValue() : null;
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