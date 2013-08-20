package li.ioc;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import li.annotation.Inject;
import li.model.Bean;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;

/**
 * Ioc加载器,加载用注解方式配置的Bean
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.2 (2012-05-08)
 */
public class AnnotationIocLoader {
    private static final Log log = Log.init();

    private static final String CLASS_REGEX = "^.*\\.class$";

    /**
     * 扫描 Source Floder 下的所有类文件, 将其中加了@Bean注解的类返回,然后被加入到IocContext
     */
    public List<Bean> getBeans() {
        File rootFolder = Files.root();
        List<String> fileList = Files.list(rootFolder, CLASS_REGEX, true);
        log.info("Found ? class files, at ?", fileList.size(), rootFolder);

        List<li.model.Bean> beans = new ArrayList<li.model.Bean>();
        for (String classFileName : fileList) {
            Class<?> type = Reflect.getType(getClassName(classFileName));
            li.annotation.Bean beanAnnotation = type.getAnnotation(li.annotation.Bean.class);
            if (beanAnnotation != null) {
                li.model.Bean iocBean = new li.model.Bean();// 一个新的Bean
                iocBean.type = type;
                iocBean.name = beanAnnotation.value();

                List<Field> fields = Reflect.getFields(type);
                for (Field field : fields) {
                    Inject inject = field.getAnnotation(Inject.class);
                    if (null != inject) {
                        li.model.Field attribute = new li.model.Field();// 一个新的Field
                        attribute.name = field.getName();
                        attribute.type = field.getType();
                        attribute.value = inject.value();
                        iocBean.fields.add(attribute);
                    }
                }
                beans.add(iocBean);

                log.debug("ADD BEAN: @Bean ? ?", type.getName(), iocBean.name);
            }
        }
        return beans;
    }

    /**
     * 取/classes/之后的字符串,替换/为.,去掉.class
     */
    private static String getClassName(String classFileName) {
        return classFileName.substring(classFileName.indexOf(File.separator + "classes" + File.separator) + 9, classFileName.length() - 6).replace(File.separatorChar, '.');
    }
}