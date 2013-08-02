package li.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 带有此注解的Field会被注入对应的Bean,或指定的值
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-05-08)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
    /**
     * 需要注入的Bean的名称或者基本类型数据的值
     * 
     * @see li.util.Verify#basicType(Class)
     */
    public String value() default "";
}