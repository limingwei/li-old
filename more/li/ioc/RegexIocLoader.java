package li.ioc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import li.model.Bean;
import li.util.Files;
import li.util.Reflect;
import li.util.Verify;

/**
 * @author : 明伟
 * @date : 2013年10月24日 下午5:39:55
 */
public class RegexIocLoader implements IocLoader {
    private static String typeRegex = Files.config().getProperty("bean.typeRegex", "~!@#none");

    private static String fieldRegex = Files.config().getProperty("bean.fieldRegex", "~!@#none");

    public List<Bean> getBeans() {
        List<String> fileList = AnnotationIocLoader.getClasseFiles();
        List<li.model.Bean> beans = new ArrayList<li.model.Bean>();
        for (String classFileName : fileList) {
            String typeName = AnnotationIocLoader.getClassName(classFileName);
            if (Verify.regex(typeName, typeRegex)) {
                li.model.Bean iocBean = new li.model.Bean();// 一个新的Bean
                iocBean.type = Reflect.getType(typeName);

                List<Field> fields = Reflect.getFields(iocBean.type);
                for (Field field : fields) {
                    if (Verify.regex(field.getName(), fieldRegex)) {
                        li.model.Field attribute = new li.model.Field();// 一个新的Field
                        attribute.name = field.getName();
                        attribute.type = field.getType();
                        iocBean.fields.add(attribute);
                    }
                }
                beans.add(iocBean);
            }
        }
        return beans;
    }
}