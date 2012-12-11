package li.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bean中带有此注解的方法为一个Action方法,会被放入ActionContext
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-05-08)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface At {
    /**
     * HTTP请求类型GET/POST/..不指定则为任意类型
     */
    public String[] method() default ".*";

    /**
     * Action请求路径,默认为方法名,可用正则表达式,如@At("thread-([0-9]*)-([a-z]*).htm"),需用括号包裹正则表达式的可变部分
     */
    public String[] value() default "";
}