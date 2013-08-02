package li.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * DataSource的简单实现,不建议在项目中使用
 * 
 * @author li (limw@w.cn)
 * @version 0.1.2 (2012-05-08)
 */
public class SimpleDataSource implements DataSource {
    private static final String[] JDBC_DRIVERS = { "com.mysql.jdbc.Driver", "org.sqlite.JDBC", "org.h2.Driver", "org.apache.derby.jdbc.EmbeddedDriver", "org.hsqldb.jdbcDriver" };

    String url;

    String username;

    String password;

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

    public SimpleDataSource() {}

    public SimpleDataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 注册JDBC驱动
     */
    public void setDriver(String driver) {
        registerDriver(driver);
    }

    public Connection getConnection() throws SQLException {
        return getConnection(this.username, this.password);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(this.url, username, password);
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
}