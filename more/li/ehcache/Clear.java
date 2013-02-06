package li.ehcache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解在方法上,使调用此方法时清理一个缓存
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-02-06)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Clear {
    /**
     * 缓存的名称 cache name
     */
    public String value() default "";
}