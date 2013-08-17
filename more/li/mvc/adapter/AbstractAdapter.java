package li.mvc.adapter;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;

import li.aop.AopChain;
import li.aop.AopFilter;
import li.mvc.Context;

/**
 * Action方法参数适配器的抽象基类
 * 
 * @author 明伟
 */
public abstract class AbstractAdapter implements AopFilter {
    public void doFilter(AopChain chain) {
        ServletRequest request = Context.getRequest();
        Method method = chain.getMethod();
        Class<?>[] argTypes = method.getParameterTypes();
        Object[] args = chain.getArgs();

        for (int i = 0; i < argTypes.length; i++) {
            if (this.match(argTypes[i])) {
                try {
                    args[i] = this.adapt(request, i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        chain.doFilter();
    }

    /**
     * 是否由当前适配器适配
     */
    public Boolean match(Class<?> type) {
        return false;
    }

    /**
     * 适配参数
     */
    public abstract Object adapt(ServletRequest request, Integer argIndex) throws Exception;
}