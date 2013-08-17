package li.util;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换的工具类
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.7 (2012-05-08)
 */
public class Convert {
    /**
     * 把字符串用一次MD5加密后返回
     */
    public static String toMD5(Object input) {
        if (Verify.isEmpty(input)) {
            return "";
        }
        try {
            StringBuffer stringBuffer = new StringBuffer();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.toString().getBytes());
            byte[] byteDigest = messageDigest.digest();
            for (int tmp, offset = 0; offset < byteDigest.length; offset++) {
                tmp = byteDigest[offset] < 0 ? byteDigest[offset] + 256 : byteDigest[offset];
                if (tmp < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(tmp));
            }
            return stringBuffer.toString(); // 32位加密
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * 将数组转换为Map,奇数位为key,偶数位为value; items必须为偶数个
     */
    public static Map<Object, Object> toMap(Object... items) {
        if (null == items || items.length % 2 != 0) {
            throw new RuntimeException("Count of items must be even !!!");// 个数必须为偶数,抛出异常
        } else {
            Map<Object, Object> map = new HashMap<Object, Object>();
            for (int i = 0; i < items.length; i = i + 2) {
                map.put(items[i], items[i + 1]);
            }
            return map;
        }
    }

    /**
     * 将字符串转换为Date
     */
    public static java.util.Date parse(Object value) {
        if (Verify.regex(value.toString().trim(), "^[0-9]{4}-[0-1]{0,1}[0-9]{1}-[0-3]{0,1}[0-9]{1} [0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
            return parse(DATE_TIME_FORMAT, value);
        } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}/[0-1]{0,1}[0-9]{1}/[0-3]{0,1}[0-9]{1} [0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
            return parse(DATE_TIME_FORMAT_2, value);
        } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}-[0-1]{0,1}[0-9]{1}-[0-3]{0,1}[0-9]{1} [0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
            return parse(DATE_TIME_FORMAT_3, value);
        } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}/[0-1]{0,1}[0-9]{1}/[0-3]{0,1}[0-9]{1} [0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
            return parse(DATE_TIME_FORMAT_4, value);
        } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}-[0-1]{0,1}[0-9]{1}-[0-3]{0,1}[0-9]{1}$")) {
            return parse(DATE_FORMAT, value);
        } else if (Verify.regex(value.toString().trim(), "^[0-9]{4}/[0-1]{0,1}[0-9]{1}/[0-3]{0,1}[0-9]{1}$")) {
            return parse(DATE_FORMAT_2, value);
        } else if (Verify.regex(value.toString().trim(), "^[0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {
            return parse(TIME_FORMAT, value);
        } else if (Verify.regex(value.toString().trim(), "^[0-2]{0,1}[0-9]{1}:[0-6]{0,1}[0-9]{1}$")) {// 表达式匹配
            return parse(TIME_FORMAT_2, value);
        } else {
            return null;
        }
    }

    /**
     * 字符串转Date
     */
    public static java.util.Date parse(DateFormat format, Object value) {
        try {// 日期时间转换
            return format.parse(value.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * 将字符串转换为时间
     */
    public static java.util.Date parse(Class<?> type, Object value) {
        if (type.equals(Time.class) && !(value instanceof Time)) {
            return new Time(parse(value).getTime());// 日期时间类型数据转换
        } else if (type.equals(Timestamp.class) && !(value instanceof Timestamp)) {
            return new Timestamp(parse(value).getTime());
        } else if (type.equals(java.sql.Date.class) && !(value instanceof java.sql.Date)) {
            return new java.sql.Date(parse(value).getTime());
        } else if (type.equals(java.util.Date.class) && !(value instanceof java.util.Date)) {
            return parse(value);
        } else {
            return (java.util.Date) value;
        }
    }

    /**
     * 将时间转换为String, java.sql.Timestamp > yyyy-MM-dd HH:mm:ss, java.sql.Time > HH:mm:ss, java.sql.Date > yyyy-MM-dd, java.util.Date yyyy-MM-dd
     */
    public static String format(java.util.Date date) {
        if (date instanceof java.sql.Timestamp) {
            return DATE_TIME_FORMAT.format(date);
        } else if (date instanceof java.sql.Time) {
            return TIME_FORMAT.format(date);
        } else {// arg instanceof java.sql.Date || arg.getClass().equals(java.util.Date.class)
            return DATE_FORMAT.format(date);
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
            } else if (java.util.Date.class.isAssignableFrom(type)) {
                return (T) parse(type, value);
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

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * yyyy/MM/dd HH:mm:ss
     */
    public static final SimpleDateFormat DATE_TIME_FORMAT_2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final SimpleDateFormat DATE_TIME_FORMAT_3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * yyyy/MM/dd HH:mm
     */
    public static final SimpleDateFormat DATE_TIME_FORMAT_4 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    /**
     * yyyy-MM-dd
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * yyyy/MM/dd
     */
    public static final SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * HH:mm:ss
     */
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * HH:mm
     */
    public static final SimpleDateFormat TIME_FORMAT_2 = new SimpleDateFormat("HH:mm");
}