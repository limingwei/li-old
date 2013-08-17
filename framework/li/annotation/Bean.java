package li.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个Ioc Bean
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-05-08)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bean {
    /**
     * 这个Bean的名称,可以留空
     */
    public String value() default "";
}