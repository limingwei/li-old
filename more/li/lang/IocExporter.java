package li.lang;

import java.io.OutputStream;
import java.util.List;

import li.ioc.AnnotationIocLoader;
import li.model.Bean;
import li.model.Field;
import li.util.IOUtil;
import li.util.Log;
import li.util.Verify;

/**
 * 将注解配置的Ioc信息导出为xml的工具类
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-08-22)
 */
public class IocExporter {
    private static final Log log = Log.init();

    /**
     * 导出注解IOC配置信息到XML
     */
    public void write(OutputStream out) {
        log.info("extract start");
        List<Bean> beans = new AnnotationIocLoader().getBeans();
        IOUtil.write(out, beansToString(beans));
        log.info("extract finished");
    }

    /**
     * 导出bean list
     */
    public String beansToString(List<Bean> beans) {
        String xmlDoc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<config>\n\t<beans>";// xml文件头
        for (Bean bean : beans) {// 每一个
            xmlDoc += beanToString(bean);// bean
        }
        xmlDoc += "\n    </beans>\n</config>";// xml文件尾
        return xmlDoc;
    }

    /**
     * 导出一个bean
     */
    private String beanToString(Bean bean) {
        String beanDoc = "\n        <bean ";
        if (!Verify.isEmpty(bean.name)) {// name
            beanDoc += "name=\"" + bean.name + "\" ";
        }
        beanDoc += "class=\"" + bean.type.getName() + "\"";// class
        String propertiesDoc = fieldsToString(bean.fields);
        if (!Verify.isEmpty(propertiesDoc)) {
            beanDoc += ">" + fieldsToString(bean.fields) + "\n        </bean>";
        } else {
            beanDoc += "/>";
        }
        return beanDoc;
    }

    private String fieldsToString(List<Field> properties) {
        String propertiesDoc = "";
        for (Field field : properties) {
            propertiesDoc += fieldToString(field);// property
        }
        return propertiesDoc;
    }

    /**
     * 导出一个property
     */
    private String fieldToString(Field field) {
        if (!Verify.isEmpty(field.value)) {// 有值
            return "\n            <property name=\"" + field.name + "\" value=\"" + field.value + "\" />";
        } else {// 无值
            return "\n            <property name=\"" + field.name + "\" />";
        }
    }
}