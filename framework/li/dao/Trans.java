package li.dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import li.util.Log;

/**
 * 事务模版工具类
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.6 (2012-05-08)
 */
public abstract class Trans {
    private static final Log log = Log.init();

    private Map<Object, Object> map;// 实例变量,用于存放一些值,可用于Trans内外通信

    /**
     * 存储当前事务中每一个Dao使用的Connection,为null意味不在事务中
     */
    protected static final ThreadLocal<Map<Class<?>, Connection>> CONNECTION_MAP = new ThreadLocal<Map<Class<?>, Connection>>();

    /**
     * 存储数据操作异常,不为null则代表出错,需要回滚
     */
    protected static final ThreadLocal<Exception> EXCEPTION = new ThreadLocal<Exception>();// 读操作时候似乎都不关紧要,考虑是否可以不要这个

    /**
     * 事务级别
     */
    protected static final ThreadLocal<Integer> LEVEL = new ThreadLocal<Integer>();

    /**
     * 事务是否只读
     */
    protected static final ThreadLocal<Boolean> READONLY = new ThreadLocal<Boolean>();

    /**
     * 定义一个事务,并执行run()中包裹的数据操作方法
     */
    public Trans() {
        this(new HashMap<Object, Object>(), null);
    }

    /**
     * 初始化一个事务,传入一些参数
     */
    public Trans(Map<Object, Object> map) {
        this(map, null);
    }

    /**
     * 初始化一个事务,并指定事务级别
     */
    public Trans(Integer level) {
        this(new HashMap<Object, Object>(), level);
    }

    /**
     * 定义并执行一个事务,传入一些参数,指定事务级别
     */
    public Trans(Map<Object, Object> map, Integer level) {
        this(map, level, false);
    }

    /**
     * 定义并执行一个事务,传入一些参数,指定事务级别,指定是否只读
     */
    public Trans(Map<Object, Object> map, Integer level, Boolean readOnly) {
        this.map = map;
        try {
            try {
                LEVEL.set(level);
                READONLY.set(readOnly);
                begin(); // 开始事务
                run(); // 执行事务内方法
                if (!readOnly) {// 可写的
                    commit(); // 提交事务
                }
                this.map.put(hashCode() + "~!@#success", true);
            } catch (Exception e) {// QueryRunner里面已经打印栈,事务管理中,也不需要抛出异常,这里就只管流程
                if (!readOnly) {// 可写的
                    rollback(); // 回滚事务
                }
                this.map.put(hashCode() + "~!@#success", false);
            } finally {
                end(); // 结束事务
            }
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
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
        if (null == CONNECTION_MAP.get()) { // Trans in Trans 时候不会重复执行
            log.trace("Trans@? level=? readOnly=? beginning", hashCode(), LEVEL.get(), READONLY.get());
            CONNECTION_MAP.set(new HashMap<Class<?>, Connection>());
        } else {
            this.map.put(hashCode() + "~!@#in_trans", true);
        }
    }

    /**
     * 结束事务,关闭当前事务中的所有Connection,如果这个事务未在其他事务中的话
     */
    private void end() throws Exception {
        Map<Class<?>, Connection> connectionMap = CONNECTION_MAP.get();
        if (null == this.map.get(hashCode() + "~!@#in_trans") && null != connectionMap) { // Trans in Trans 时候不会重复执行
            for (Entry<Class<?>, Connection> connection : connectionMap.entrySet()) {
                connection.getValue().close();
                log.trace("Closing Connection in Trans ?", connection.getValue());
            }
            CONNECTION_MAP.set(null);
            EXCEPTION.set(null);
            LEVEL.set(null);
            log.trace("Trans@? level=? readOnly=? ending", hashCode(), LEVEL.get(), READONLY.get());
        }
    }

    /**
     * 捆绑提交当前事务中所有Connection的事务,如果这个事务未在其他事务中的话
     */
    private void commit() throws Exception {
        Map<Class<?>, Connection> connectionMap = CONNECTION_MAP.get();
        if (null == this.map.get(hashCode() + "~!@#in_trans") && null != connectionMap) {
            for (Entry<Class<?>, Connection> connection : connectionMap.entrySet()) {
                connection.getValue().commit();
                log.trace("Trans@? level=? readOnly=? commiting ?", hashCode(), LEVEL.get(), READONLY.get(), connection.getValue());
            }
        }
    }

    /**
     * 捆绑回滚当前事务中所有Connection中的事务,如果这个事务未在其他事务中的话
     */
    private void rollback() throws Exception {
        Map<Class<?>, Connection> connectionMap = CONNECTION_MAP.get();
        if (null == this.map.get(hashCode() + "~!@#in_trans") && null != connectionMap) {
            for (Entry<Class<?>, Connection> connection : connectionMap.entrySet()) {
                connection.getValue().rollback();
                log.trace("Trans@?  level=? readOnly=?  rollingback ?", hashCode(), LEVEL.get(), READONLY.get(), connection.getValue());
            }
        }
    }
}