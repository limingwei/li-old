package li.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志工具类,自动适配Log4j或Console
 * 
 * @author li (limw@w.cn)
 * @version 0.1.6 (2012-07-05)
 */
public abstract class Log {
    /**
     * Log初始化方法,自动适配Log4j或Console
     */
    public static Log init(final Class<?> type) {
        try {
            return new Log() {// 尝试初始化Log4J
                Object logger = Reflect.call("org.apache.log4j.Logger", "getLogger", new Class[] { Class.class }, new Object[] { type });

                protected void log(String method, Object msg, Object... args) {
                    try {
                        if (!(method.equals("debug") || method.equals("info") || method.equals("trace")) || Reflect.invoke(logger, "is" + method.substring(0, 1).toUpperCase() + method.substring(1) + "Enabled").equals(true)) {
                            logger.getClass().getMethod(method, Object.class).invoke(logger, process(msg, args));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Exception at li.util.Log.init().new Log() {}.log(String, Object)", e);
                    }
                };
            };
        } catch (Throwable e) {
            return new Log() {// 返回ConsoleLog
                protected void log(String method, Object msg, Object... args) {
                    if (method.toUpperCase().equals("ERROR") || method.toUpperCase().equals("FATAL")) {
                        System.err.println(method.toUpperCase() + ": " + process(msg, args));
                    } else {
                        System.out.println(method.toUpperCase() + ": " + process(msg, args));
                    }
                }
            };
        }
    }

    /**
     * 处理log信息
     */
    private static String process(Object msg, Object... args) {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        if ("li.dao.Trans".equals(traces[5].getClassName())) {
            msg = "calling by " + traces[8].getClassName() + "." + traces[8].getMethodName() + "() #" + traces[8].getLineNumber() + " " + msg;
        }
        return traces[5].getMethodName() + "() #" + traces[5].getLineNumber() + " " + msg;
    }

    /**
     * 抽象方法,由不同的Log做具体的适配
     */
    protected abstract void log(String method, Object msg, Object... args);

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
     * 输出TRACE级别的日志 Level 1
     */
    public void trace(Object msg, Object... args) {
        log("trace", msg, args);
    }

    /**
     * 输出DEBUG级别的日志 Level 2
     */
    public void debug(Object msg, Object... args) {
        log("debug", msg, args);
    }

    /**
     * 输出INFO级别的日志 Level 3
     */
    public void info(Object msg, Object... args) {
        log("info", msg, args);
    }

    /**
     * 输出WARN级别的日志 Level 4
     */
    public void warn(Object msg, Object... args) {
        log("warn", msg, args);
    }

    /**
     * 输出ERROR级别的日志 Level 5
     */
    public void error(Object msg, Object... args) {
        log("error", msg, args);
    }

    /**
     * 输出FATAL级别的日志 Level 6
     */
    public void fatal(Object msg, Object... args) {
        log("fatal", msg, args);
    }

    /**
     * 一个缓存,可用于暂时保存一个值
     */
    private static final Map LOG_MAP = new HashMap();

    /**
     * 向LOG_MAP中设值,synchronized方法
     */
    public synchronized static void put(Object key, Object value) {
        LOG_MAP.put(key, value);
    }

    /**
     * 从LOG_MAP中取值,synchronized方法
     */
    public synchronized static Object get(Object key) {
        return LOG_MAP.get(key);
    }
}