package li.util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * 日志工具类,自动适配Log4j或Console
 * 
 * @author li (limingwei@mail.com)
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
     * 第一句log
     */
    static {
        init("-- li's framework --").info("Fork me on https://github.com/limingwei/li  bugs on https://github.com/limingwei/li/issues");
    }

    /**
     * Log初始化方法,根据类名初始化Log,自动适配Log4j或Console
     */
    public static Log init(final String className) {
        try {
            return new Log() {// 尝试初始化Log4J
                Logger logger = Logger.getLogger(className);

                protected void log(String level, Object message) {
                    logger.log(Priority.toPriority(level), message);
                }
            };
        } catch (Throwable e) {
            if (null == Log.get("start")) {
                Log.put("start", System.currentTimeMillis());
            }
            return new Log() {// 返回ConsoleLog
                protected void log(String level, Object message) {
                    ("FATAL".equals(level) || "ERROR".equals(level) ? System.err : System.out).println("Log:[" + Thread.currentThread().getName() + "] " + (System.currentTimeMillis() - (Long) Log.get("start")) + " " + level + " " + Convert.format(Convert.DATE_TIME_FORMAT_5, new Date()) + " " + className + " " + message);
                }
            };
        }
    }

    /**
     * 根据类型初始化Log
     */
    public static Log init(final Class<?> type) {
        return init(type.getName());
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
    protected abstract void log(String level, Object message);

    /**
     * 处理log信息
     */
    private static String process(Object message, Object... args) {
        if (null == args || args.length < 1 || !message.toString().contains("?")) {
            return message + "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        char[] chars = null == message ? new char[0] : message.toString().toCharArray();
        int arg_index = 0;
        for (int i = 0; i < chars.length; i++) {
            stringBuffer.append((arg_index < args.length && chars[i] == '?') ? args[arg_index++] : chars[i]);
        }
        return stringBuffer.toString();
    }

    /**
     * 输出TRACE级别的日志 Level 1
     */
    public void trace(Object message, Object... args) {
        log("TRACE", process(message, args));
    }

    /**
     * 输出DEBUG级别的日志 Level 2
     */
    public void debug(Object message, Object... args) {
        log("DEBUG", process(message, args));
    }

    /**
     * 输出INFO级别的日志 Level 3
     */
    public void info(Object message, Object... args) {
        log("INFO", process(message, args));
    }

    /**
     * 输出WARN级别的日志 Level 4
     */
    public void warn(Object message, Object... args) {
        log("WARN", process(message, args));
    }

    /**
     * 输出ERROR级别的日志 Level 5
     */
    public void error(Object message, Object... args) {
        log("ERROR", process(message, args));
    }

    /**
     * 输出FATAL级别的日志 Level 6
     */
    public void fatal(Object message, Object... args) {
        log("FATAL", process(message, args));
    }
}