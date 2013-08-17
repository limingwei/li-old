package li.ehcache;

import java.util.concurrent.locks.Lock;

import li.aop.AopChain;
import li.aop.AopFilter;

/**
 * CacheAopFilter,缓存的name为方法,key为方法参数,value为方法返回值
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2013-02-05)
 */
public class CacheFilter implements AopFilter {
    public void doFilter(AopChain chain) {
        li.ehcache.Clear clear = chain.getMethod().getAnnotation(li.ehcache.Clear.class);
        if (null == clear) {
            doCache(chain);// 缓存执行方法
        } else {
            CacheUtil.removeCache(clear.value());// 清理缓存
        }
    }

    /**
     * 缓存执行方法
     */
    private void doCache(AopChain chain) {
        String cacheName = CacheUtil.cacheName(chain.getMethod());
        String cacheKey = CacheUtil.cacheKey(chain.getArgs());
        Object cacheData = CacheUtil.getElement(cacheName, cacheKey);
        if (cacheData == null) {
            cacheData = doMethod(cacheName, cacheKey, cacheData, chain);// 执行目标方法
            CacheUtil.putElement(cacheName, cacheKey, cacheData);// 缓存方法返回值
        }
        chain.setResult(cacheData);// 不执行目标方法,设置他的返回值
    }

    /**
     * 执行目标方法(事实上会进行二次锁检测是否需要执行)
     */
    private Object doMethod(String cacheName, Object cacheKey, Object cacheData, AopChain chain) {
        Lock lock = CacheUtil.getLock(cacheName);
        lock.lock();
        try {
            cacheData = CacheUtil.getElement(cacheName, cacheKey);
            if (cacheData == null) {
                cacheData = chain.doFilter().getResult();// 这里执行了目标方法
            }
        } finally {
            lock.unlock();
        }
        return cacheData;
    }
}