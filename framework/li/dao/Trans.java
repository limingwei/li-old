package li.dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import li.util.Log;

/**
 * 事务模版工具类
 * 
 * @author li (limw@w.cn)
 * @version 0.1.6 (2012-05-08)
 */
public abstract class Trans {
    private static final Log log = Log.init();

    private Map map;// 实例变量,用于存放一些值,可用于Trans内外通信

    /**
     * 存储当前事务中使用到的Connection,为null意味不在事务中
     */
    public static final ThreadLocal<Map<Class, Connection>> CONNECTION_MAP = new ThreadLocal<Map<Class, Connection>>();

    /**
     * 存储数据操作异常,不为null则代表出错,需要回滚
     */
    public static final ThreadLocal<Exception> EXCEPTION = new ThreadLocal<Exception>();

    /**
     * 定义一个事务,并执行run()中包裹的数据操作方法
     */
    public Trans() {
        this(new HashMap());
    }

    /**
     * 定义并执行一个事务,并传入一些参数
     */
    public Trans(Map map) {
        this.map = map;
        try {
            begin(); // 开始事务
            run(); // 执行事务内方法
            if (null == EXCEPTION.get()) { // 如果没有出现错误
                commit(); // 提交事务
                this.map.put(hashCode() + "~!@#success", true);
            } else {// 如果出现错误
                rollback(); // 回滚事务
                this.map.put(hashCode() + "~!@#success", false);
            }
            end(); // 结束事务
        } catch (Exception e) {
            throw new RuntimeException("Exception in trans", e);
        }
    }

    /**
     * 抽象方法,包裹需要事务控制的Dao方法
     */
    public abstract void run();

    /**
     * 返回map引用
     */
    public Map<Object, Object> map() {
        return this.map;
    }

    /**
     * 返回事务执行成功与否的标记
     */
    public Boolean success() {
        return (Boolean) this.map.get(hashCode() + "~!@#success");
    }

    /**
     * 开始事务,初始化CONNECTION_MAP,或者标记这个事务已被其他事务包裹融化
     */
    private void begin() {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[5];
        if (null == CONNECTION_MAP.get()) { // Trans in Trans 时候不会重复执行
            log.debug("Trans@" + hashCode() + " begin in " + trace.getClassName() + "." + trace.getMethodName() + "() #" + trace.getLineNumber());
            CONNECTION_MAP.set(new HashMap<Class, Connection>());
        } else {
            this.map.put(hashCode() + "~!@#in_trans", true);
        }
    }

    /**
     * 结束事务,关闭当前事务中的所有Connection,如果这个事务未在其他事务中的话
     */
    private void end() throws Exception {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[5];
        Map<Class, Connection> connectionMap = CONNECTION_MAP.get();
        if (null == this.map.get(hashCode() + "~!@#in_trans") && null != connectionMap) { // Trans in Trans 时候不会重复执行
            for (Entry<Class, Connection> connection : connectionMap.entrySet()) {
                connection.getValue().close();
                log.trace("Closing Connection in Trans " + connection.getValue());
            }
            CONNECTION_MAP.set(null);
            EXCEPTION.set(null);
            log.debug("Trans@" + hashCode() + " end in " + trace.getClassName() + "." + trace.getMethodName() + "() #" + trace.getLineNumber());
        }
    }

    /**
     * 捆绑提交当前事务中所有Connection的事务,如果这个事务未在其他事务中的话
     */
    private void commit() throws Exception {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[5];
        Map<Class, Connection> connectionMap = CONNECTION_MAP.get();
        if (null == this.map.get(hashCode() + "~!@#in_trans") && null != connectionMap) {
            for (Entry<Class, Connection> connection : connectionMap.entrySet()) {
                connection.getValue().commit();
                log.debug("Trans@" + hashCode() + " commit " + connection.getValue() + " in " + trace.getClassName() + "." + trace.getMethodName() + "() #" + trace.getLineNumber());
            }
        }
    }

    /**
     * 捆绑回滚当前事务中所有Connection中的事务,如果这个事务未在其他事务中的话
     */
    private void rollback() throws Exception {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[5];
        Map<Class, Connection> connectionMap = CONNECTION_MAP.get();
        if (null == this.map.get(hashCode() + "~!@#in_trans") && null != connectionMap) {
            for (Entry<Class, Connection> connection : connectionMap.entrySet()) {
                connection.getValue().rollback();
                log.debug("Trans@" + hashCode() + " rollback " + connection.getValue() + " in " + trace.getClassName() + "." + trace.getMethodName() + "() #" + trace.getLineNumber());
            }
        }
    }
}