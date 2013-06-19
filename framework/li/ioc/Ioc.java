package li.ioc;

import java.lang.reflect.Type;

import li.aop.AopEnhancer;
import li.aop.AopFilter;
import li.model.Bean;
import li.model.Field;
import li.util.Log;
import li.util.Reflect;
import li.util.Verify;

/**
 * Ioc工具类,用于从IocContext中得到一个对象
 * 
 * @author li (limw@w.cn)
 * @version 0.1.3 (2012-05-08)
 */
public class Ioc {
    private static final Log log = Log.init();

    private static Boolean AOP_CAN = false;

    /**
     * 检测Aop功能是否可用,只执行一次
     */
    static {
        try {
            Class.forName("net.sf.cglib.proxy.Enhancer");
            AOP_CAN = true;
        } catch (Exception e) {}
    }

    /**
     * 检查Bean是否已经实例化,若无,则实例化之
     */
    public static Bean init(Bean bean) {
        if (null == bean.instance) {// 如果尚未实例化
            if (!AOP_CAN || AopFilter.class.isAssignableFrom(bean.type)) {
                log.trace("Ioc initializing ?", bean.type);
                bean.instance = Reflect.born(bean.type);
            } else {
                log.trace("Aop initializing ?", bean.type);
                bean.instance = AopEnhancer.create(bean.type);
            }

            for (Field field : bean.fields) {
                if (Verify.basicType(field.type)) {// 基本类型,直接设值
                    Reflect.set(bean.instance, field.name, field.value);
                } else {// 非基本类型,设为相应的bean
                    Reflect.set(bean.instance, field.name, Ioc.get(field.type, field.value));
                }
                log.trace("Set Field: ?.? = ?", bean.type.getName(), field.name, field.value);
            }
        }
        return bean;
    }

    /**
     * 若一个Bean为type类型或其子类型,则返回他的实例
     */
    public static <T> T get(Class<T> type) {
        for (Bean bean : IocContext.getInstance().BEANS) {
            if (type.isAssignableFrom(bean.type)) {
                return (T) init(bean).instance;
            }
        }
        return null;
    }

    /**
     * 若Bean名称直接匹配则返回
     */
    public static <T> T get(String name) {
        for (Bean bean : IocContext.getInstance().BEANS) {
            if (!Verify.isEmpty(name) && bean.name.equals(name)) {
                return (T) init(bean).instance;
            }
        }
        return null;
    }

    /**
     * 返回名称和类型均符合的Bean,若没有,则返回类型符合的一个Bean
     */
    public static <T> T get(Class<T> type, String name) {
        if (!Verify.isEmpty(name) && null != type) {
            for (Bean bean : IocContext.getInstance().BEANS) {
                if (type.isAssignableFrom(bean.type) && bean.name.equals(name)) {
                    return (T) init(bean).instance;
                }
            }
        }
        return get(type);// 如果name为空则使用GetByName查找
    }

    /**
     * 若类型匹配,且泛型参数的实际类型为genericType,则返回他的实例
     * 
     * @param genericType Bean泛型类型
     */
    public static <T> T get(Class<T> type, Type genericType) {
        for (Bean bean : IocContext.getInstance().BEANS) {
            if (type.isAssignableFrom(bean.type) && null != genericType && genericType.equals(Reflect.actualType(bean.type, 0))) {
                return (T) init(bean).instance;
            }
        }
        return null;
    }
}