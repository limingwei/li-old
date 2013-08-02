package li.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志工具类,自动适配Log4j或Console
 * 
 * @author li (limw@w.cn)
 * @version 0.1.6 (2012-07-05)
 */
public abstract class Log {
    /**
     * 一个缓存,可用于暂时保存一个值
     */
    private static final Map<Object, Object> LOG_MAP = new ConcurrentHashMap<Object, Object>();

    /**
     * 向LOG_MAP中设值
     */
    public static void put(Object key, Object value) {
        LOG_MAP.put(key, value);
    }

    /**
     * 从LOG_MAP中取值
     */
    public static Object get(Object key) {
        return LOG_MAP.get(key);
    }

    /**
     * Log初始化方法,自动适配Log4j或Console
     */
    public static Log init(final Class<?> type) {
        try {
            return new Log() {// 尝试初始化Log4J
                Object logger = Reflect.call("org.apache.log4j.Logger", "getLogger", new Class[] { Class.class }, new Object[] { type });

                protected void log(String method, Object msg) {
                    Reflect.invoke(logger, method, new Class<?>[] { Object.class }, new Object[] { msg });
                }
            };
        } catch (Throwable e) {
            return new Log() {// 返回ConsoleLog
                protected void log(String method, Object msg) {
                    ((method.equals("error") || method.equals("fatal")) ? System.err : System.out).println(method.toUpperCase() + ": " + msg);
                }
            };
        }
    }

    /**
     * 根据类名初始化Log
     */
    public static Log init(String className) {
        return init(Reflect.getType(className));
    }

    /**
     * 初始化Log最简单的方法,会自动获取调用者的类型
     */
    public static Log init() {
        return init(Thread.currentThread().getStackTrace()[2].getClassName());
    }

    /**
     * 抽象方法,由不同的Log做具体的适配
     */
    protected abstract void log(String method, Object msg);

    /**
     * 处理log信息
     */
    private static String process(Object msg, Object... args) {
        if (null == args || args.length < 1) {
            return msg + "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        char[] chars = null == msg ? new char[0] : msg.toString().toCharArray();
        int arg_index = 0;
        for (int i = 0; i < chars.length; i++) {
            stringBuffer.append((arg_index < args.length && chars[i] == '?') ? args[arg_index++] : chars[i]);
        }
        return stringBuffer.toString();
    }

    /**
     * 输出TRACE级别的日志 Level 1
     */
    public void trace(Object msg, Object... args) {
        log("trace", process(msg, args));
    }

    /**
     * 输出DEBUG级别的日志 Level 2
     */
    public void debug(Object msg, Object... args) {
        log("debug", process(msg, args));
    }

    /**
     * 输出INFO级别的日志 Level 3
     */
    public void info(Object msg, Object... args) {
        log("info", process(msg, args));
    }

    /**
     * 输出WARN级别的日志 Level 4
     */
    public void warn(Object msg, Object... args) {
        log("warn", process(msg, args));
    }

    /**
     * 输出ERROR级别的日志 Level 5
     */
    public void error(Object msg, Object... args) {
        log("error", process(msg, args));
    }

    /**
     * 输出FATAL级别的日志 Level 6
     */
    public void fatal(Object msg, Object... args) {
        log("fatal", process(msg, args));
    }
}