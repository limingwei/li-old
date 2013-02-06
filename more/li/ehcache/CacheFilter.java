package li.ehcache;

import java.util.concurrent.locks.Lock;

import li.aop.AopChain;
import li.aop.AopFilter;

/**
 * CacheAopFilter
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-02-05)
 */
public class CacheFilter implements AopFilter {
    /**
     * 
     */
    public void doFilter(AopChain chain) {
        String cacheName = CacheUtil.cacheName(chain.getMethod());
        String cacheKey = CacheUtil.cacheKey(chain.getArgs());
        Object cacheData = CacheUtil.getElement(cacheName, cacheKey);
        if (cacheData == null) {
            Lock lock = CacheUtil.getLock(cacheName);
            lock.lock();
            try {
                cacheData = CacheUtil.getElement(cacheName, cacheKey);
                if (cacheData == null) {
                    CacheUtil.putElement(cacheName, cacheKey, chain.doFilter().getResult());
                }
            } finally {
                lock.unlock();
            }
        } else {
            chain.setResult(cacheData);
        }
    }
}