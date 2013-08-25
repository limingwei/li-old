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
     * 日志级别
     */
    private static final Integer DEBUG = 10000, INFO = 20000, WARN = 30000, ERROR = 40000, FATAL = 50000, TRACE = 5000;

    /**
     * 日志级别 数组索引*5000 即是 int类型的级别
     */
    private static final String[] LEVELS = { "", "TRACE", "DEBUG", "", "INFO", "", "WARN", "", "ERROR", "", "FATAL" };

    /**
     * 第一句log
     */
    static {
        init("-- li's framework --").info("Fork me on https://github.com/limingwei/li, bugs on https://github.com/limingwei/li/issues");
    }

    /**
     * Log初始化方法,根据类名初始化Log,自动适配Log4j或Console
     */
    public static Log init(final String className) {
        try {
            return new Log() {// 尝试初始化Log4J
                Logger logger = Logger.getLogger(className);

                protected void log(Integer level, Object message) {
                    logger.log(Priority.toPriority(level), message);
                }
            };
        } catch (Throwable e) {
            final Object start = Log.get("start");
            if (null == start) {
                Log.put("start", System.currentTimeMillis());
            }
            return new Log() {// 返回ConsoleLog
                protected void log(Integer level, Object message) {
                    ((level > 25000) ? System.err : System.out).println("Log:[" + Thread.currentThread().getName() + "] " + (System.currentTimeMillis() - (Long) start) + " " + LEVELS[level / 5000] + " " + Convert.format(Convert.DATE_TIME_FORMAT_5, new Date()) + " " + className + " " + message);
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
    protected abstract void log(Integer level, Object message);

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
        log(TRACE, process(msg, args));
    }

    /**
     * 输出DEBUG级别的日志 Level 2
     */
    public void debug(Object msg, Object... args) {
        log(DEBUG, process(msg, args));
    }

    /**
     * 输出INFO级别的日志 Level 3
     */
    public void info(Object msg, Object... args) {
        log(INFO, process(msg, args));
    }

    /**
     * 输出WARN级别的日志 Level 4
     */
    public void warn(Object msg, Object... args) {
        log(WARN, process(msg, args));
    }

    /**
     * 输出ERROR级别的日志 Level 5
     */
    public void error(Object msg, Object... args) {
        log(ERROR, process(msg, args));
    }

    /**
     * 输出FATAL级别的日志 Level 6
     */
    public void fatal(Object msg, Object... args) {
        log(FATAL, process(msg, args));
    }
}