package li.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import li.util.Log;
import li.util.ThreadUtil;

/**
 * DataSource的简单实现
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.2 (2012-05-08)
 */
public class SimpleDataSource implements DataSource {
    private static final String[] JDBC_DRIVERS = { "com.mysql.jdbc.Driver", "org.sqlite.JDBC", "org.h2.Driver", "org.apache.derby.jdbc.EmbeddedDriver", "org.hsqldb.jdbcDriver" };

    private static final Log log = Log.init();

    private String url;

    private String username;

    private String password;

    /**
     * 获取链接的计数器
     */
    private int times = 0;

    /**
     * 打印trace的正则表达式
     */
    private String regex = "默认的全都不打印";

    /**
     * 是否打印trace
     */
    private Boolean trace = false;// 默认页不显示链接个数提醒

    /**
     * 被代理dataSource
     */
    private DataSource dataSource;

    /**
     * 当前持有未管理的链接
     */
    private List<ConnectionWrapper> unclosedConnections = new ArrayList<ConnectionWrapper>();

    /**
     * 日志流
     */
    private PrintWriter logWriter;

    /**
     * 静态初始化代码块,注册数据库驱动
     */
    static {
        for (String driver : JDBC_DRIVERS) {
            registerDriver(driver);
        }
    }

    /**
     * 注册驱动
     */
    private static void registerDriver(String driver) {
        try {
            Class.forName(driver);
        } catch (Exception e) {}
    }

    /**
     * 打印泄漏链接堆栈
     */
    private void traceConnections() {
        String string = "";
        for (ConnectionWrapper connection : unclosedConnections) {
            string += "\n" + connection + " Called by " + ThreadUtil.stackTrace(connection.getStackTrace(), this.regex);
        }
        log.trace(string);
    }

    /**
     * 移除一个未关闭链接
     */
    protected void removeConnection(Connection connection) {
        unclosedConnections.remove(connection);
        if (trace) {
            log.debug("Closing a link, totally got " + (++times) + " links , there are " + unclosedConnections.size() + " unclosed links");
            if (unclosedConnections.size() > 0) {
                traceConnections();
            }
        }
    }

    /**
     * 注册JDBC驱动
     */
    public void setDriver(String driver) {
        registerDriver(driver);
    }

    /**
     * 是否打印链接获取日志
     */
    public void setTrace(Boolean trace) {
        this.trace = trace;
    }

    /**
     * 打印泄漏链接时候类名方法名筛选
     */
    public void setRegex(String regex) {
        this.regex = regex;
        this.trace = true;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 配置 user 或 username 都可以
     */
    public void setUser(String user) {
        this.username = user;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置被代理DataSource
     */
    public void setDataSource(DataSource dataSource) {
        log.info("Setting the to be agented dataSource ?", dataSource.getClass().getName() + "@" + Integer.toHexString(dataSource.hashCode()));
        this.dataSource = dataSource;
    }

    /**
     * 不支持切换用户
     */
    public Connection getConnection(String username, String password) throws SQLException {
        log.warn("不支持使用新的用户名密码获取连接");
        return this.getConnection();
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null == this.dataSource ? DriverManager.getConnection(this.url, this.username, this.password) : this.dataSource.getConnection();

        ConnectionWrapper connectionWrapper = new ConnectionWrapper(this, connection);
        this.unclosedConnections.add(connectionWrapper);
        if (trace) {
            log.debug("Getting a link, totally got " + (++times) + " links , there are " + this.unclosedConnections.size() + " unclosed links");
            if (this.unclosedConnections.size() > 1) {
                traceConnections();
            }
        }
        return connectionWrapper;
    }

    public PrintWriter getLogWriter() throws SQLException {
        if (null != this.dataSource) {
            return dataSource.getLogWriter();
        }
        return this.logWriter;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        if (null != this.dataSource) {
            this.dataSource.setLogWriter(out);
        } else {
            this.logWriter = out;
        }
    }

    public int getLoginTimeout() throws SQLException {
        if (null != this.dataSource) {
            return this.getLoginTimeout();
        }
        return DriverManager.getLoginTimeout();
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        if (null != this.dataSource) {
            this.dataSource.setLoginTimeout(seconds);
        } else {
            DriverManager.setLoginTimeout(seconds);
        }
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (null != this.dataSource) {
            return this.dataSource.isWrapperFor(iface);
        }
        return null != iface && iface.isInstance(this);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (null != this.dataSource) {
            return this.dataSource.unwrap(iface);
        }
        return null != iface && iface.isInstance(this) ? (T) this : null;
    }

    public String toString() {
        return super.toString() + " url=" + this.url;
    }
}