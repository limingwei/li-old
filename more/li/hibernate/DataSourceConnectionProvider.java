package li.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import li.dao.Trans;

import org.hibernate.HibernateException;
import org.hibernate.connection.ConnectionProvider;

/**
 * @author 明伟
 */
public class DataSourceConnectionProvider implements ConnectionProvider {
    public void configure(Properties properties) throws HibernateException {}

    public DataSource getDataSource() {
        return SessionFactory.DATASOURCE_THREADLOCAL.get();
    }

    public Connection getConnection() throws SQLException {
        Connection connection;
        try {
            Trans trans = Trans.current();
            if (null == trans) {
                connection = this.getDataSource().getConnection();
            } else {
                connection = trans.getConnection(this.getDataSource());
            }
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public void close() throws HibernateException {}

    public boolean supportsAggressiveRelease() {
        return false;
    }
}