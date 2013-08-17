package li.model;

import java.lang.reflect.Method;

import li.annotation.Arg;

/**
 * Action对象,表示一个Action
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.5 (2012-05-08)
 * @see li.annotation.At
 */
public class Action {
    /**
     * Action请求路径
     */
    public String path;

    /**
     * HTTP请求类型
     */
    public String httpMethod;

    /**
     * Action类的实例
     */
    public Object actionInstance;

    /**
     * Action方法对象
     */
    public Method actionMethod;

    /**
     * Action方法形参名列表
     */
    public String[] argNames;

    /**
     * Action方法参数类型列表
     */
    public Class<?>[] argTypes;

    /**
     * Action方法参数注解列表
     */
    public Arg[] argAnnotations;
}