package li.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解在一个类上,使类里的测试方法中的数据库操作会自动回滚
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-01-14)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Rollback {}