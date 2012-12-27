package li.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import li.annotation.Aop;
import li.ioc.Ioc;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Aop增强类生成器,依赖CGLIB
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-09-20)
 */
public class AopEnhancer {
    /**
     * 内置的AopFilter,用li.dao.Trans包裹执行chain.doFilter,使被包裹的方法在事务中执行
     */
    private static final AopFilter TRANS_FILTER = new AopFilter() {
        public void doFilter(final AopChain chain) {
            new li.dao.Trans() {
                public void run() {
                    chain.doFilter();
                }
            };
        }
    };

    /**
     * 自定义的NamingPolicy,使Aop子类类名以$Aop结尾
     */
    private static final NamingPolicy NAMING_POLICY = new NamingPolicy() {
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            prefix = null == prefix ? "net.sf.cglib.empty.Object" : prefix.startsWith("java") ? "$" + prefix : prefix;
            return source.endsWith("Enhancer") ? prefix + "$Aop" : prefix + "$FastClass";
        } // http://www.oschina.net/code/explore/cglib-2.2/src/proxy/net/sf/cglib/core/DefaultNamingPolicy.java
    };

    /**
     * 生成一个Aop增强的对象
     */
    public static Object create(Class<?> type) {
        final Map<Method, List<AopFilter>> filtersMap = new HashMap<Method, List<AopFilter>>();// 构造这个类型所有方法的AopFilter的集合
        Method[] methods = type.getDeclaredMethods();
        for (Method method : methods) {// 对每一个方法
            List<AopFilter> filters = new ArrayList<AopFilter>();
            Aop aop = method.getAnnotation(Aop.class);
            for (int length = (null == aop ? -1 : aop.value().length), i = 0; i < length; i++) {// 如果有@Aop注解,对每一个@Aop.value()的值
                filters.add(Ioc.get(aop.value()[i]));
            }
            if (null != method.getAnnotation(li.annotation.Trans.class)) {// 如果有@Trans注解
                filters.add(TRANS_FILTER);
            }
            filtersMap.put(method, filters);
        }
        Enhancer enhancer = new Enhancer(); // 创建代理
        enhancer.setNamingPolicy(NAMING_POLICY);
        enhancer.setSuperclass(type);
        enhancer.setCallback(new MethodInterceptor() {// 设置callback,使用AopChain代理执行方法
            public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return new AopChain(target, method, args, filtersMap.get(method), proxy).doFilter().getResult();
            }// 使用AopChian代理执行这个方法并返回值
        });
        return enhancer.create();// 创建代理对象
    }
}