package li.lang;

import java.io.File;
import java.util.List;

import li.ioc.AnnotationIocLoader;
import li.model.Bean;
import li.model.Field;
import li.util.FileUtil;
import li.util.Log;
import li.util.Verify;

/**
 * 将注解配置的Ioc信息导出为xml的工具类
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-08-22)
 */
public class IocExporter {
    private static final Log log = Log.init();

    /**
     * 导出注解IOC配置信息到XML
     * 
     * @param file 导出IOC的文件
     */
    public void extract(File file) {
        log.info("extract start");
        List<Bean> beans = new AnnotationIocLoader().getBeans();
        FileUtil.write(file, doc(beans));
        log.info("extract finished");
    }

    /**
     * 导出bean list
     */
    private String doc(List<Bean> beans) {
        String xmlDoc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<config>\n\t<beans>";// xml文件头
        for (Bean bean : beans) {// 每一个
            xmlDoc += bean(bean);// bean
        }
        xmlDoc += "\n\t</beans>\n</config>";// xml文件尾
        return xmlDoc;
    }

    /**
     * 导出一个bean
     */
    private String bean(Bean bean) {
        String beanDoc = "\n\t\t<bean ";
        if (!Verify.isEmpty(bean.name)) {// name
            beanDoc += "name=\"" + bean.name + "\" ";
        }
        beanDoc += "class=\"" + bean.type.getName() + "\">";// class
        for (Field field : bean.fields) {
            beanDoc += property(field);// property
        }
        beanDoc += "\n\t\t</bean>";
        return beanDoc;
    }

    /**
     * 导出一个property
     */
    private String property(Field field) {
        if (!Verify.isEmpty(field.value)) {// 有值
            return "\n\t\t\t<property name=\"" + field.name + "\" value=\"" + field.value + "\" />";
        } else {// 无值
            return "\n\t\t\t<property name=\"" + field.name + "\" />";
        }
    }
}