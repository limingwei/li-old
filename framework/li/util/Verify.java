package li.util;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.5 (2012-05-08)
 */
public class Verify {
    /**
     * 判断type是否基本类型,如String,Boolean,Number,Date
     * 
     * @see li.util.Reflect#set(Object, String, Object)
     */
    public static Boolean basicType(Class<?> type) {
        if (type.isArray()) {// 如是数组
            return basicType(type.getComponentType());// 判断元素类型
        } else {
            return type.isPrimitive() || type.equals(String.class) || type.equals(Boolean.class) || Number.class.isAssignableFrom(type) || java.util.Date.class.isAssignableFrom(type);
        }
    }

    /**
     * 判断一个字符串是否为空,不为null且长度大于0时返回true
     */
    public static Boolean isEmpty(Object object) {
        return null == object || (object instanceof String && ((String) object).trim().length() < 1);
    }

    /**
     * 判断是否两字符串均不为空且string以str开始,不区分大小写
     */
    public static Boolean startWith(String string, String str) {
        return !isEmpty(string) && !isEmpty(str) && (string.trim().toUpperCase().startsWith(str.trim().toUpperCase()));
    }

    /**
     * 判断是否两字符串均不为空且string以str结尾,不区分大小写
     */
    public static Boolean endWith(String string, String str) {
        return !isEmpty(string) && !isEmpty(str) && (string.trim().toUpperCase().endsWith(str.trim().toUpperCase()));
    }

    /**
     * 判断是否两字符串均不为空且str是string的一部分,不区分大小写
     */
    public static Boolean contain(String string, String str) {
        return !isEmpty(string) && !isEmpty(str) && (string.trim().toUpperCase().contains(str.trim().toUpperCase()));
    }

    /**
     * 验证字符串是否符合正则表达式
     */
    public static Boolean regex(String input, String regex) {
        return Pattern.compile(regex).matcher(input).find();
    }
}