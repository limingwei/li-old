package li.ioc;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import li.model.Bean;
import li.util.Reflect;
import li.util.Strings;
import li.util.Verify;

/**
 * Ioc工具类,用于从IocContext中得到一个对象
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.3 (2012-05-08)
 */
public class Ioc {
    private static final Map<Object, Object> CACHE = new ConcurrentHashMap<Object, Object>();

    /**
     * 若一个Bean为type类型或其子类型,则返回他的实例
     */
    public static <T> T get(Class<T> type) {
        Object target = CACHE.get(type);
        if (null == target) {
            for (Bean bean : IocContext.getInstance().getBeans()) {
                if (type.isAssignableFrom(bean.type)) {
                    CACHE.put(type, bean.instance);
                    return (T) bean.instance;
                }
            }
        }
        return (T) target;
    }

    /**
     * 若Bean名称直接匹配则返回
     */
    public static <T> T get(String name) {
        Object target = CACHE.get(name);
        if (null == target) {
            for (Bean bean : IocContext.getInstance().getBeans()) {
                if (!Verify.isEmpty(name) && bean.name.equals(name)) {
                    CACHE.put(name, bean.instance);
                    return (T) bean.instance;
                }
            }
        }
        return (T) target;
    }

    /**
     * 返回名称和类型均符合的Bean,若没有,则返回类型符合的一个Bean
     */
    public static <T> T get(Class<T> type, String name) {
        String key = type + "#" + name;
        Object target = CACHE.get(key);
        if (null == target) {
            if (!Verify.isEmpty(name) && null != type) {
                for (Bean bean : IocContext.getInstance().getBeans()) {
                    if (type.isAssignableFrom(bean.type) && bean.name.equals(name)) {
                        CACHE.put(key, bean.instance);
                        return (T) bean.instance;
                    }
                }
            }
        }
        return null == target ? get(type) : (T) target;// 如果name为空则使用GetByType查找
    }

    /**
     * 若类型匹配,且泛型参数的实际类型为genericType,则返回他的实例
     * 
     * @param genericTypes Bean泛型类型
     */
    public static <T> T get(Class<T> type, Type... genericTypes) {
        String key = Strings.link("#", genericTypes);
        Object target = CACHE.get(key);
        if (null == target) {
            for (Bean bean : IocContext.getInstance().getBeans()) {
                if (type.isAssignableFrom(bean.type) && null != genericTypes && Arrays.equals(genericTypes, Reflect.actualTypes(bean.type))) {
                    CACHE.put(key, bean.instance);
                    return (T) bean.instance;
                }
            }
        }
        return (T) target;
    }
}