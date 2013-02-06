package li.ehcache;

import li.aop.AopChain;
import li.aop.AopFilter;

/**
 * CacheAopFilter
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-02-05)
 */
public class ClearCacheFilter implements AopFilter {
    /**
     * 
     */
    public void doFilter(AopChain chain) {
        String cacheName = CacheUtil.cacheName(chain.getMethod());
        CacheUtil.removeCache(cacheName);
    }
}