package li.test;

import java.lang.reflect.Field;

import li.annotation.Inject;
import li.dao.Page;
import li.ioc.Ioc;
import li.util.Reflect;
import li.util.Verify;

/**
 * 测试类的基类，会为每一个@Inject注解的字段设值
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-07-21)
 */
public class BaseTest {
    /**
     * 模拟的 li.dao.Page
     */
    protected Page page;

    /**
     * 初始化时为每一个@Inject注解的属性设值
     */
    public BaseTest() {
        this.page = new Page();

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Inject inject = field.getAnnotation(Inject.class);
            if (null != inject) {
                if (Verify.basicType(field.getType())) {
                    Reflect.set(this, field.getName(), inject.value());
                } else {
                    Reflect.set(this, field.getName(), Ioc.get(field.getType(), inject.value()));
                }
            }
        }
    }
}