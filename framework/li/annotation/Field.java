package li.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记类中与数据库中表的一列相对应的一个Field
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.2 (2012-05-08)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
    /**
     * 此Field对应于数据库中数据表的字段名,默认为Field名
     */
    public String value() default "";
}