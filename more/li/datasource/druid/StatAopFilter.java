package li.datasource.druid;

import li.aop.AopChain;
import li.aop.AopFilter;

import com.alibaba.druid.filter.stat.StatFilterContext;
import com.alibaba.druid.filter.stat.StatFilterContextListenerAdapter;
import com.alibaba.druid.support.spring.stat.SpringMethodInfo;
import com.alibaba.druid.support.spring.stat.SpringMethodStat;
import com.alibaba.druid.support.spring.stat.SpringStat;
import com.alibaba.druid.support.spring.stat.SpringStatManager;

/**
 * com.alibaba.druid.support.spring.stat.DruidStatInterceptor
 * 
 * @author 明伟
 */
public class StatAopFilter implements AopFilter {
    private static SpringStat springStat = new SpringStat();

    public StatAopFilter() throws Exception {
        SpringStatManager.getInstance().addSpringStat(springStat);
        StatFilterContext.getInstance().addContextListener(new SpringMethodContextListener());
    }

    public void doFilter(AopChain chain) {
        SpringMethodStat lastMethodStat = SpringMethodStat.current();
        SpringMethodStat methodStat = springStat.getMethodStat(new SpringMethodInfo(chain.getMethod().getDeclaringClass(), chain.getMethod()), true);
        if (methodStat != null) {
            methodStat.beforeInvoke();
        }
        Throwable error = null;
        long startNanos = System.nanoTime();
        try {
            chain.doFilter();
        } catch (Throwable e) {
            throw new RuntimeException(e + " ", error = e);
        } finally {
            long nanos = System.nanoTime() - startNanos;
            if (methodStat != null) {
                methodStat.afterInvoke(error, nanos);
            }
            SpringMethodStat.setCurrent(lastMethodStat);
        }
    }

    private class SpringMethodContextListener extends StatFilterContextListenerAdapter {
        public void addUpdateCount(int updateCount) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcUpdateCount(updateCount);
            }
        }

        public void addFetchRowCount(int fetchRowCount) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcFetchRowCount(fetchRowCount);
            }
        }

        public void executeBefore(String sql, boolean inTransaction) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcExecuteCount();
            }
        }

        public void executeAfter(String sql, long nanos, Throwable error) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcExecuteTimeNano(nanos);
                if (error != null) {
                    springMethodStat.incrementJdbcExecuteErrorCount();
                }
            }
        }

        public void commit() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcCommitCount();
            }
        }

        public void rollback() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.incrementJdbcRollbackCount();
        }

        public void pool_connect() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcPoolConnectionOpenCount();
            }
        }

        public void pool_close(long nanos) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcPoolConnectionCloseCount();
            }
        }

        public void resultSet_open() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcResultSetOpenCount();
            }
        }

        public void resultSet_close(long nanos) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcResultSetCloseCount();
            }
        }
    }
}