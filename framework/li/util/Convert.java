package li.util;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换的工具类
 * 
 * @author li (limw@w.cn)
 * @version 0.1.7 (2012-05-08)
 */
public class Convert {

    /**
     * 把字符串用一次MD5加密后返回
     */
    public static String toMD5(Object input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.toString().getBytes());
            byte[] byteDigest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer("");
            int i;
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset] < 0 ? byteDigest[offset] + 256 : byteDigest[offset];
                if (i < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(i));
            }
            return stringBuffer.toString(); // 32位加密
        } catch (Exception e) {
            throw new RuntimeException("Exception at li.util.Convert.toMD5(Object)", e);
        }
    }

    /**
     * 将数组转换为Map,奇数位为key,偶数位为value; items必须为偶数个
     */
    public static Map<Object, Object> toMap(Object... items) {
        if (null == items || items.length % 2 != 0) {
            throw new RuntimeException("Count of items must be even !!!");// 个数必须为偶数,抛出异常
        } else {
            Map map = new HashMap();
            for (int i = 0; i < items.length; i = i + 2) {
                map.put(items[i], items[i + 1]);
            }
            return map;
        }
    }

    /**
     * 把传入的value转换为type类型
     */
    public static <T> T toType(Class<T> type, Object value) {
        if (null != type && null != value && value.toString().length() > 0) {// 两参数均不为空
            if ((type.equals(Integer.TYPE) || type.equals(Integer.class)) && !(value instanceof Integer)) {// 基本类型数据转换
                return (T) Integer.valueOf(value.toString().trim());
            } else if ((type.equals(Boolean.TYPE) || type.equals(Boolean.class)) && !(value instanceof Boolean)) {
                return (T) Boolean.valueOf(value.toString().trim());
            } else if ((type.equals(Long.TYPE) || type.equals(Long.class)) && !(value instanceof Long)) {
                return (T) Long.valueOf(value.toString().trim());
            } else if ((type.equals(Float.TYPE) || type.equals(Float.class)) && !(value instanceof Float)) {
                return (T) Float.valueOf(value.toString().trim());
            } else if ((type.equals(Double.TYPE) || type.equals(Double.class)) && !(value instanceof Double)) {
                return (T) Double.valueOf(value.toString().trim());
            } else if ((type.equals(Short.TYPE) || type.equals(Short.class)) && !(value instanceof Short)) {
                return (T) Short.valueOf(value.toString().trim());
            } else if ((type.equals(Byte.TYPE) || type.equals(Byte.class)) && !(value instanceof Byte)) {
                return (T) Byte.valueOf(value.toString().trim());
            } else if ((type.equals(Character.TYPE) || type.equals(Character.class)) && !(value instanceof Character)) {
                return (T) Character.valueOf(value.toString().trim().charAt(0));
            } else if (type.equals(Time.class) && !(value instanceof Time)) {
                return (T) new Time(toType(java.util.Date.class, value).getTime());// 日期时间类型数据转换
            } else if (type.equals(Timestamp.class) && !(value instanceof Timestamp)) {
                return (T) new Timestamp(toType(java.util.Date.class, value).getTime());
            } else if (type.equals(java.sql.Date.class) && !(value instanceof java.sql.Date)) {
                return (T) new java.sql.Date(toType(java.util.Date.class, value).getTime());
            } else if (type.equals(java.util.Date.class) && !(value instanceof java.util.Date)) {
                String pattern = "";
                if (Verify.regex(value.toString().trim(), "^[0-9]{4}/[0-1]{0,1}[0-9]{1}/[0-3]{0,1}[0-9]{1} [0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
                    pattern = "yyyy/MM/dd HH:mm:ss";
                } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}-[0-1]{0,1}[0-9]{1}-[0-3]{0,1}[0-9]{1} [0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
                    pattern = "yyyy-MM-dd HH:mm:ss";
                } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}/[0-1]{0,1}[0-9]{1}/[0-3]{0,1}[0-9]{1} [0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
                    pattern = "yyyy/MM/dd HH:mm";
                } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}-[0-1]{0,1}[0-9]{1}-[0-3]{0,1}[0-9]{1} [0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
                    pattern = "yyyy-MM-dd HH:mm";
                } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}/[0-1]{0,1}[0-9]{1}/[0-3]{0,1}[0-9]{1}$")) {
                    pattern = "yyyy/MM/dd";
                } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}-[0-1]{0,1}[0-9]{1}-[0-3]{0,1}[0-9]{1}$")) {
                    pattern = "yyyy-MM-dd";
                } else if (Verify.regex(value.toString().trim(), "^[0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
                    pattern = "HH:mm:ss";
                } else if (Verify.regex(value.toString().trim(), "^[0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {// 表达式匹配
                    pattern = "HH:mm";
                }
                try {// 日期时间转换
                    return (T) new SimpleDateFormat(pattern).parse(value.toString());
                } catch (ParseException e) {}
            }
        }
        return (T) value;// 缺省的返回方式
    }

    /**
     * 将数组中的每个元素进行类型转换
     * 
     * @param type type不能是基本数据类型 Primitive
     */
    public static <T> T[] toType(Class<T> type, Object... values) {
        T[] dest = null;
        if (null != type && null != values && values.length > 0) {// 参数不为空且数组大小不为0
            if (type == Integer.TYPE) {
                type = (Class<T>) Integer.class;// 将原始数据类型转换为其封装类型
            } else if (type == Boolean.TYPE) {
                type = (Class<T>) Boolean.class;
            } else if (type == Long.TYPE) {
                type = (Class<T>) Long.class;
            } else if (type == Float.TYPE) {
                type = (Class<T>) Float.class;
            } else if (type == Double.TYPE) {
                type = (Class<T>) Double.class;
            } else if (type == Short.TYPE) {
                type = (Class<T>) Short.class;
            } else if (type == Byte.TYPE) {
                type = (Class<T>) Byte.class;
            } else if (type == Character.TYPE) {
                type = (Class<T>) Character.class;
            }
            dest = (T[]) Array.newInstance(type, values.length);// 生成目标类型数组
            for (int i = 0; i < values.length; i++) {
                dest[i] = (T) toType(type, values[i]);// 类型转换每一个元素
            }
        }
        return dest;
    }
}