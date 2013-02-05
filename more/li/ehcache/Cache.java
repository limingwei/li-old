package li.ehcache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解缓存的名称
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-02-05)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {
    /**
     * 缓存的名称 cache name
     */
    public String value();
}