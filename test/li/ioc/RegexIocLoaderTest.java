package li.ioc;

import java.util.List;

import li.model.Bean;
import li.model.Field;
import li.util.Reflect;

import org.junit.Test;

public class RegexIocLoaderTest {
    @Test
    public void getBeans() {
        IocLoader iocLoader = new RegexIocLoader();
        Reflect.set(iocLoader, "typeRegex", "li.people[.](action|record)[.].*");
        Reflect.set(iocLoader, "fieldRegex", ".*(Service|Dao|dataSource)");

        List<Bean> beans = iocLoader.getBeans();
        System.err.println(beans);
        for (Bean bean : beans) {
            System.err.println("bean\t" + bean.name + "\t" + bean.type);
            for (Field field : bean.fields) {
                System.err.println("field\t\t" + field.name + "\t" + field.type + "\t" + field.value);
            }
        }
    }
}