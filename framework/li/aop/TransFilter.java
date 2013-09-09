package li.aop;

import java.sql.Connection;
import java.util.HashMap;

import li.util.Reflect;

/**
 * 内置的AopFilter,用li.dao.Trans包裹执行chain.doFilter,使被包裹的方法在事务中执行
 * 
 * @author 明伟
 */
public class TransFilter implements AopFilter {
    private Integer level;

    private Boolean readOnly;

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setIsolation(String isolation) {
        this.setLevel((Integer) Reflect.getStatic(Connection.class, isolation));
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void doFilter(final AopChain chain) {
        new li.dao.Trans(new HashMap<Object, Object>(), this.level, this.readOnly) {
            public void run() {
                chain.doFilter();
            }
        };
    }
}