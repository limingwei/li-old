package li.ioc;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import li.model.Bean;
import li.model.Field;
import li.util.Files;
import li.util.Log;

/**
 * Ioc容器,保存所有的Bean
 * 
 * @author li (limw@w.cn)
 * @version 0.1.5 (2012-05-08)
 * @see li.ioc.Ioc
 */
public class IocContext {
    private static final Log log = Log.init();

    private static IocContext IOC_CONTEXT = null;// 存储IocContext的实例,它会是单例的

    /**
     * List,用于保存所有的BEAN
     */
    public final List<Bean> BEANS = new CopyOnWriteArrayList<Bean>();

    /**
     * 得到一个单例的IocContext对象,包含通过不同方式配置的Bean集合,在List<Bean> BEANS里面
     */
    public static synchronized IocContext getInstance() {
        if (IOC_CONTEXT == null) {
            Log.put("IOCSTARTUP", System.currentTimeMillis());

            IOC_CONTEXT = new IocContext();

            // STEP-1-使用XmlIocLoader和AnnotationIocLoader初始化IocContext,即添加Beans
            IOC_CONTEXT.BEANS.addAll(new XmlIocLoader().getBeans());
            IOC_CONTEXT.BEANS.addAll(new AnnotationIocLoader().getBeans());

            // STEP-2-处理field.value中得 ${name}
            Properties properties = Files.load("config.properties");
            for (Bean bean : IOC_CONTEXT.BEANS) {
                for (Field field : bean.fields) {// 如果 filed.value 形如 ${name} 则使用 properties 中key为name的值替换
                    if (field.value.length() > 3 && field.value.startsWith("${") && field.value.endsWith("}")) {
                        field.value = properties.getProperty(field.value.replace("${", "").replace("}", ""));
                    }
                }
            }

            log.debug(IOC_CONTEXT.BEANS.size() + " beans started up in " + (System.currentTimeMillis() - (Long) Log.get("IOCSTARTUP")) + "ms");
        }
        return IOC_CONTEXT;
    }
}