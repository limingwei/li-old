package li.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解一个Action方法的参数,参数是基本数据类型,基本类型数组,Page,Record或者POJO
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-07-20)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Arg {
    /**
     * 基本类型parameter的Key或者对象的parameter的Key前缀
     */
    public String value();
}