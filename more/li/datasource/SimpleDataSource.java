package li.datasource;

import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.sql.DataSource;

import li.util.Log;
import li.util.ThreadUtil;

/**
 * DataSource的简单实现,不建议在项目中使用
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
     * 静态初始化代码块,注册数据库驱动
     */
    static {
        for (String driver : JDBC_DRIVERS) {
            registerDriver(driver);
        }
    }

    private static void registerDriver(String driver) {
        try {
            Class.forName(driver);
        } catch (Exception e) {}
    }

    /**
     * 注册JDBC驱动
     */
    public void setDriver(String driver) {
        registerDriver(driver);
    }

    public void setTrace(Boolean trace) {
        this.trace = trace;
    }

    public void setRegex(String regex) {
        this.regex = regex;
        this.trace = true;
    }

    /**
     * 配置 user 或 username 都可以
     */
    public void setUser(String user) {
        this.username = user;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDataSource(DataSource dataSource) {
        log.info("Setting the to be agented dataSource ?", dataSource.getClass().getName() + "@" + Integer.toHexString(dataSource.hashCode()));
        this.dataSource = dataSource;
    }

    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = null == this.dataSource ? DriverManager.getConnection(this.url, username, password) : this.dataSource.getConnection(username, password);

        ConnectionWrapper connectionWrapper = new ConnectionWrapper(this, connection);
        unclosedConnections.add(connectionWrapper);
        if (trace) {
            log.debug("Getting a link, totally got " + (++times) + " links , there are " + unclosedConnections.size() + " unclosed links");
            if (unclosedConnections.size() > 1) {
                traceConnections();
            }
        }
        return connectionWrapper;
    }

    public void removeConnection(Connection connection) {
        unclosedConnections.remove(connection);
        if (trace) {
            log.debug("Closing a link, totally got " + (++times) + " links , there are " + unclosedConnections.size() + " unclosed links");
            if (unclosedConnections.size() > 0) {
                traceConnections();
            }
        }
    }

    private void traceConnections() {
        String string = "";
        for (ConnectionWrapper connection : unclosedConnections) {
            string += "\n" + connection + " Called by " + ThreadUtil.stackTrace(connection.getStackTrace(), this.regex);
        }
        log.trace(string);
    }

    public Connection getConnection() throws SQLException {
        return getConnection(this.username, this.password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return new PrintWriter(System.out);
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new RuntimeException("no implement method");
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new RuntimeException("no implement method");
    }

    public int getLoginTimeout() throws SQLException {
        throw new RuntimeException("no implement method");
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        throw new RuntimeException("no implement method");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new RuntimeException("no implement method");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new RuntimeException("no implement method");
    }

    public String toString() {
        return super.toString() + " " + this.url;
    }

    /**
     * ConnectionWrapper
     * 
     * @author : li (limingwei@mail.com)
     */
    public class ConnectionWrapper implements Connection {
        private SimpleDataSource dataSource;

        private Connection connection;

        private StackTraceElement[] stackTrace;

        public ConnectionWrapper(SimpleDataSource dataSource, Connection connection) {
            this.dataSource = dataSource;
            this.connection = connection;
            this.stackTrace = Thread.currentThread().getStackTrace();
        }

        public StackTraceElement[] getStackTrace() {
            return this.stackTrace;
        }

        public void close() throws SQLException {
            connection.close();
            dataSource.removeConnection(this);
        }

        public <T> T unwrap(Class<T> iface) throws SQLException {
            return connection.unwrap(iface);
        }

        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return connection.isWrapperFor(iface);
        }

        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return connection.prepareStatement(sql);
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            return connection.prepareCall(sql);
        }

        public String nativeSQL(String sql) throws SQLException {
            return connection.nativeSQL(sql);
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);
        }

        public boolean getAutoCommit() throws SQLException {
            return connection.getAutoCommit();
        }

        public void commit() throws SQLException {
            connection.commit();
        }

        public void rollback() throws SQLException {
            connection.rollback();
        }

        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            return connection.getMetaData();
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
            connection.setReadOnly(readOnly);
        }

        public boolean isReadOnly() throws SQLException {
            return connection.isReadOnly();
        }

        public void setCatalog(String catalog) throws SQLException {
            connection.setCatalog(catalog);
        }

        public String getCatalog() throws SQLException {
            return connection.getCatalog();
        }

        public void setTransactionIsolation(int level) throws SQLException {
            connection.setTransactionIsolation(level);
        }

        public int getTransactionIsolation() throws SQLException {
            return connection.getTransactionIsolation();
        }

        public SQLWarning getWarnings() throws SQLException {
            return connection.getWarnings();
        }

        public void clearWarnings() throws SQLException {
            connection.clearWarnings();
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return connection.getTypeMap();
        }

        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            connection.setTypeMap(map);
        }

        public void setHoldability(int holdability) throws SQLException {
            connection.setHoldability(holdability);
        }

        public int getHoldability() throws SQLException {
            return connection.getHoldability();
        }

        public Savepoint setSavepoint() throws SQLException {
            return connection.setSavepoint();
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            return connection.setSavepoint(name);
        }

        public void rollback(Savepoint savepoint) throws SQLException {
            connection.rollback(savepoint);
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            connection.releaseSavepoint(savepoint);
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return connection.prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return connection.prepareStatement(sql, columnNames);
        }

        public Clob createClob() throws SQLException {
            return connection.createClob();
        }

        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        public NClob createNClob() throws SQLException {
            return connection.createNClob();
        }

        public SQLXML createSQLXML() throws SQLException {
            return connection.createSQLXML();
        }

        public boolean isValid(int timeout) throws SQLException {
            return connection.isValid(timeout);
        }

        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            connection.setClientInfo(name, value);
        }

        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            connection.setClientInfo(properties);
        }

        public String getClientInfo(String name) throws SQLException {
            return connection.getClientInfo(name);
        }

        public Properties getClientInfo() throws SQLException {
            return connection.getClientInfo();
        }

        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return connection.createArrayOf(typeName, elements);
        }

        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return connection.createStruct(typeName, attributes);
        }

        public void setSchema(String schema) throws SQLException {
            connection.setSchema(schema);
        }

        public String getSchema() throws SQLException {
            return connection.getSchema();
        }

        public void abort(Executor executor) throws SQLException {
            connection.abort(executor);
        }

        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            connection.setNetworkTimeout(executor, milliseconds);
        }

        public int getNetworkTimeout() throws SQLException {
            return connection.getNetworkTimeout();
        }
    }
}