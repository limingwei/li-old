package li.ioc;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import li.aop.AopEnhancer;
import li.aop.AopFilter;
import li.model.Bean;
import li.model.Field;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;
import li.util.Verify;

/**
 * Ioc容器,保存所有的Bean
 * 
 * @author li (limw@w.cn)
 * @version 0.1.5 (2012-05-08)
 * @see li.ioc.Ioc
 */
public class IocContext {
    private static final Log log = Log.init();

    private static final AopEnhancer AOP_ENHANCER = new AopEnhancer();

    private static IocContext IOC_CONTEXT = null;// 存储IocContext的实例,它会是单例的

    /**
     * List,用于保存所有的BEAN
     */
    private final List<Bean> beans = new CopyOnWriteArrayList<Bean>();

    /**
     * 返回所有IocBean
     */
    public List<Bean> getBeans() {
        return this.beans;
    }

    /**
     * 得到一个单例的IocContext对象,包含通过不同方式配置的Bean集合,在List<Bean> BEANS里面
     */
    public static/* synchronized */IocContext getInstance() {
        if (IOC_CONTEXT == null) {
            Long start = System.currentTimeMillis();

            IOC_CONTEXT = new IocContext();

            // STEP-1-使用XmlIocLoader和AnnotationIocLoader添加Beans
            IOC_CONTEXT.beans.addAll(new XmlIocLoader().getBeans());
            IOC_CONTEXT.beans.addAll(new AnnotationIocLoader().getBeans());

            // STEP-2-处理field.value中的${name}
            Properties properties = Files.load("config.properties");
            for (Bean bean : IOC_CONTEXT.beans) {
                for (Field field : bean.fields) {// 如果 filed.value 形如 ${name} 则使用 properties 中key为name的值替换
                    if (field.value.length() > 3 && field.value.startsWith("${") && field.value.endsWith("}")) {
                        field.value = properties.getProperty(field.value.replace("${", "").replace("}", ""));
                    }
                }
            }

            // STEP-3-实例化所有的bean
            for (Bean bean : IOC_CONTEXT.beans) {
                if (null == bean.instance) {// 如果尚未实例化
                    if (!AOP_ENHANCER.aopCan || AopFilter.class.isAssignableFrom(bean.type)) {
                        log.trace("Ioc initializing ?", bean.type);
                        bean.instance = Reflect.born(bean.type);
                    } else {
                        log.trace("Aop initializing ?", bean.type);
                        bean.instance = AOP_ENHANCER.create(bean.type);
                    }
                }
            }

            // STEP-4-为所有的bean设置属性
            for (Bean bean : IOC_CONTEXT.beans) {
                for (Field field : bean.fields) {
                    if (Verify.basicType(field.type)) {// 基本类型,直接设值
                        Reflect.set(bean.instance, field.name, field.value);
                    } else {// 非基本类型,设为相应的bean
                        Reflect.set(bean.instance, field.name, Ioc.get(field.type, field.value));
                    }
                    log.trace("Set Field: ?.? = ?", bean.type.getName(), field.name, field.value);
                }
            }

            log.debug("? beans started up in ?ms", IOC_CONTEXT.beans.size(), (System.currentTimeMillis() - start));
        }
        return IOC_CONTEXT;
    }
}