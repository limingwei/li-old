package li.aop;

import java.sql.Connection;
import java.util.HashMap;

import li.util.Reflect;
import li.util.Verify;

/**
 * 内置的AopFilter,用li.dao.Trans包裹执行chain.doFilter,使被包裹的方法在事务中执行
 * 
 * @author 明伟
 */
public class TransFilter implements AopFilter {
    /**
     * 事务隔离级别
     */
    private Integer level;

    /**
     * 事务是否只读
     */
    private Boolean readOnly;

    /**
     * 事务级别,参数可以为String常量名 TRANSACTION_READ_UNCOMMITTED 也可以为他的Int值 1
     */
    public void setLevel(String level) {
        if (!Verify.isEmpty(level)) {
            try {
                this.level = Integer.valueOf(level);
            } catch (Exception e) {
                this.level = (Integer) Reflect.getStatic(Connection.class, level);
            }
        }
    }

    /**
     * 默认为false
     */
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