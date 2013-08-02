package li.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个Action方法
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-05-08)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface At {
    /**
     * Action请求路径,默认为方法名
     */
    public String[] value() default "";

    /**
     * HTTP请求类型GET/POST/..不指定则为GET
     */
    public String[] method() default "GET";
}