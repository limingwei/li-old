package li.aop;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

/**
 * Aop方法执行链
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-09-20)
 */
public class AopChain {
    private Object target;// 目标对象

    private Method method;// 方法对象

    private Object[] args;// 参数数组

    private Object result;// 方法返回值

    private List<AopFilter> filters;// AopFilter列表

    private MethodProxy proxy;// 方法代理

    private int index = 0;// AopFilter索引

    /**
     * 返回被代理方法宿主对象
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * 返回被代理方法
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * 返回方法参数
     */
    public Object[] getArgs() {
        return this.args;
    }

    /**
     * 设置方法参数,在doChain之前才有效
     */
    public AopChain setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    /**
     * 返回方法返回值,在方法执行后才有值
     */
    public Object getResult() {
        return this.result;
    }

    /**
     * 设置方法返回值,设置后不再doChain才有效
     */
    public AopChain setResult(Object result) {
        this.result = result;
        return this;
    }

    /**
     * 初始化一个AopChain
     */
    public AopChain(Object target, Method method, Object[] args, List<AopFilter> filters, MethodProxy proxy) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.filters = filters;
        this.proxy = proxy;
    }

    /**
     * 执行AopChain,执行下一个AopFilter或者执行被代理方法
     */
    public AopChain doFilter() {
        if (null == filters || index >= filters.size()) {// 如果没有AopFilter或者已经经过全部AopFilter
            invoke();// 执行目标方法
        } else {// 还有AopFilter
            filters.get(index++).doFilter(this);// 执行第index个AopFilter然后index++
        }
        return this;
    }

    /**
     * 执行目标方法
     */
    public AopChain invoke() {
        try {
            this.result = proxy.invokeSuper(target, args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}