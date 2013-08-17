package li.aop;

/**
 * AopFilter接口,AopFilter实现类的方法不能Aop
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-09-20)
 */
public interface AopFilter {
    /**
     * AopFilter的实现类需要实现的方法
     */
    public void doFilter(AopChain chain);
}