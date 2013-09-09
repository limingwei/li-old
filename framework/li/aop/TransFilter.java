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

    /**
     * 事务级别,参数可以为String常量名 TRANSACTION_READ_UNCOMMITTED 也可以为他的Int值 1
     */
    public void setLevel(String level) {
        try {
            this.level = Integer.valueOf(level);
        } catch (Exception e) {
            this.level = (Integer) Reflect.getStatic(Connection.class, level);
        }
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