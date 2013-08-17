package li.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import li.aop.AopFilter;

/**
 * 注解一个需要Aop支持的方法
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-09-20)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Aop {
    /**
     * AopFilter的类型列表,会依据此处类型查找AopFilter的Bean
     */
    public Class<? extends AopFilter>[] value();
}