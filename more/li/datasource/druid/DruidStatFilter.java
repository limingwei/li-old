package li.datasource.druid;

import java.lang.reflect.Method;

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
public class DruidStatFilter implements AopFilter {
    public static final String PROP_NAME_PORFILE = "druid.profile";
    private static SpringStat springStat = new SpringStat();

    private SpringMethodContextListener statContextlistener = new SpringMethodContextListener();

    public void afterPropertiesSet() throws Exception {
        SpringStatManager.getInstance().addSpringStat(springStat);

        StatFilterContext.getInstance().addContextListener(this.statContextlistener);
    }

    public void destroy() throws Exception {
        StatFilterContext.getInstance().removeContextListener(this.statContextlistener);
    }

    public void doFilter(AopChain chain) {
        System.err.println("DruidStatFilter.doFilter() --> " + chain.getTarget().getClass() + "." + chain.getMethod().getName() + "()");
        SpringMethodStat lastMethodStat = SpringMethodStat.current();

        SpringMethodInfo methodInfo = getMethodInfo(chain);

        SpringMethodStat methodStat = springStat.getMethodStat(methodInfo, true);

        if (methodStat != null) {
            System.err.println("methodStat.beforeInvoke();");
            methodStat.beforeInvoke();
        }
        long startNanos = System.nanoTime();
        Throwable error = null;
        try {
            System.err.println("chain.doFilter();");
            chain.doFilter();
        } catch (Throwable e) {
            throw new RuntimeException(e + " ", e);
        } finally {
            long endNanos = System.nanoTime();
            long nanos = endNanos - startNanos;
            if (methodStat != null) {
                System.err.println("methodStat.afterInvoke(error, nanos);");
                methodStat.afterInvoke(error, nanos);
            }
            SpringMethodStat.setCurrent(lastMethodStat);
        }
    }

    public SpringMethodInfo getMethodInfo(AopChain chain) {
        Method method = chain.getMethod();
        return new SpringMethodInfo(method.getDeclaringClass(), method);
    }

    class SpringMethodContextListener extends StatFilterContextListenerAdapter {
        SpringMethodContextListener() {}

        public void addUpdateCount(int updateCount) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.addJdbcUpdateCount(updateCount);
        }

        public void addFetchRowCount(int fetchRowCount) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.addJdbcFetchRowCount(fetchRowCount);
        }

        public void executeBefore(String sql, boolean inTransaction) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.incrementJdbcExecuteCount();
        }

        public void executeAfter(String sql, long nanos, Throwable error) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcExecuteTimeNano(nanos);
                if (error != null)
                    springMethodStat.incrementJdbcExecuteErrorCount();
            }
        }

        public void commit() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.incrementJdbcCommitCount();
        }

        public void rollback() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.incrementJdbcRollbackCount();
        }

        public void pool_connect() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.incrementJdbcPoolConnectionOpenCount();
        }

        public void pool_close(long nanos) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.incrementJdbcPoolConnectionCloseCount();
        }

        public void resultSet_open() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.incrementJdbcResultSetOpenCount();
        }

        public void resultSet_close(long nanos) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null)
                springMethodStat.incrementJdbcResultSetCloseCount();
        }
    }
}