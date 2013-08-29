package li.aop;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import li.annotation.Aop;
import li.ioc.Ioc;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;
import li.util.Verify;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.w3c.dom.NodeList;

/**
 * Aop增强类生成器,依赖CGLIB
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-09-20)
 */
public class AopEnhancer {
    private static final Log log = Log.init();
    /**
     * AopXml配置文件
     */
    private static final String AOP_CONFIG_REGEX = "^.*(config|ioc|aop)\\.xml$";

    /**
     * 内置AopFilter
     */
    private final Map<String, AopFilter> filtersBuiltIn = new HashMap<String, AopFilter>();

    /**
     * Xml配置的Aop规则
     */
    private final List<String[]> xmlAopRules = new ArrayList<String[]>();

    /**
     * 自定义的NamingPolicy,使Aop子类类名以$Aop结尾
     */
    private NamingPolicy namingPolicy;

    /**
     * 内置的AopFilter,用li.dao.Trans包裹执行chain.doFilter,使被包裹的方法在事务中执行
     */
    private AopFilter transFilter;

    /**
     * 初始化
     */
    public AopEnhancer() {
        namingPolicy = new NamingPolicy() {// 自定义的NamingPolicy,使Aop子类类名以$Aop结尾
            public String getClassName(String prefix, String source, Object key, Predicate names) {
                prefix = null == prefix ? "net.sf.cglib.empty.Object" : prefix.startsWith("java") ? "$" + prefix : prefix;
                return source.endsWith("Enhancer") ? prefix + "$Aop" : prefix + "$FastClass";
            } // http://t.cn/zQo4ydN
        };

        transFilter = new AopFilter() {// 内置的AopFilter,用li.dao.Trans包裹执行chain.doFilter,使被包裹的方法在事务中执行
            public void doFilter(final AopChain chain) {
                new li.dao.Trans() {
                    public void run() {
                        chain.doFilter();
                    }
                };
            }
        };

        filtersBuiltIn.put("~!@#trans", transFilter);// 内置AopFilter

        // 解析XmlAop配置
        File rootFolder = Files.root();
        List<String> fileList = Files.list(rootFolder, AOP_CONFIG_REGEX, true, 1);// 搜索配置文件
        log.info("Found ? aop config xml files, at ?", fileList.size(), rootFolder);

        for (String filePath : fileList) {
            NodeList beanNodes = (NodeList) Files.xpath(Files.build(filePath), "//aop", XPathConstants.NODESET);
            for (int length = (null == beanNodes ? -1 : beanNodes.getLength()), i = 0; i < length; i++) {
                String type = Files.xpath(beanNodes.item(i), "@class", XPathConstants.STRING).toString();
                String method = Files.xpath(beanNodes.item(i), "@method", XPathConstants.STRING).toString();
                String aops = Files.xpath(beanNodes.item(i), "@filter", XPathConstants.STRING).toString();
                xmlAopRules.add(new String[] { type, method, aops });
            }
        }
    }

    /**
     * 获取一个方法通过注解配置的AopFilters
     */
    private List<AopFilter> getAnnotationFilters(Method method) {
        List<AopFilter> filters = new ArrayList<AopFilter>();
        Aop aop = method.getAnnotation(Aop.class);
        for (int length = (null == aop ? -1 : aop.value().length), i = 0; i < length; i++) {// 如果有@Aop注解,对每一个@Aop.value()的值
            AopFilter filter = Ioc.get(aop.value()[i]);// 通过Ioc得到AopFilter
            if (null == filter) {
                filter = Reflect.born(aop.value()[i]);// 非Ioc管理,直接new
            }
            if (null != filter) {
                filters.add(filter);
            }
        }
        if (null != method.getAnnotation(li.annotation.Trans.class)) {// 如果有@Trans注解
            filters.add(transFilter);
        }
        return filters;
    }

    /**
     * 获取一个方法通过Xml配置的AopFilters
     */
    private List<AopFilter> getXmlFilters(Object target, Method method) {
        List<AopFilter> filters = new ArrayList<AopFilter>();
        for (String[] role : xmlAopRules) {// 所有的规则
            if (Verify.regex(target.getClass().getName(), role[0]) && Verify.regex(method.getName(), role[1])) {// method.getDeclaringClass() or target.getClass()
                String[] names = role[2].split(",");// 所有Aop切入类
                for (String name : names) {
                    AopFilter filter = Ioc.get(name);// 通过Ioc得到AopFilter
                    if (null == filter) {
                        filter = filtersBuiltIn.get("~!@#" + name);// 内置的AopFilter
                    }
                    if (null != filter) {
                        filters.add(filter);
                    }
                }
            }
        }
        return filters;
    }

    /**
     * 获取一个方法的所有AopFilters
     */
    private List<AopFilter> getFilters(Object target, Method method) {
        List<AopFilter> filters = getAnnotationFilters(method);
        filters.addAll(getXmlFilters(target, method));
        return filters;
    }

    /**
     * 生成一个Aop增强的对象
     */
    public Object create(Class<?> type) {
        Enhancer enhancer = new Enhancer(); // 创建代理
        enhancer.setNamingPolicy(namingPolicy);
        enhancer.setSuperclass(type);
        enhancer.setCallback(new MethodInterceptor() {// 设置callback,使用AopChain代理执行方法
            public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return new AopChain(target, method, args, getFilters(target, method), proxy).doFilter().getResult();
            }// 使用AopChian代理执行这个方法并返回值
        });
        return enhancer.create();// 创建代理对象
    }
}