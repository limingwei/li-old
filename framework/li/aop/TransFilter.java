package li.aop;

/**
 * 内置的AopFilter,用li.dao.Trans包裹执行chain.doFilter,使被包裹的方法在事务中执行
 * 
 * @author 明伟
 */
public class TransFilter implements AopFilter {
    private Integer level;

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void doFilter(final AopChain chain) {
        new li.dao.Trans(this.level) {
            public void run() {
                chain.doFilter();
            }
        };
    }
}