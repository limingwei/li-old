package li.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记与数据库中一张表相对应的一个类,POJO或者Record
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-05-08)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 与所标记的类对应的数据表的表名,留空则使用类名
     */
    public String value() default "";

    /**
     * 标记表的id列名,默认为id
     */
    public String id() default "id";
}