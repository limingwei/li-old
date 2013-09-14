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

    private DataSource dataSource;

    public DataSource getDataSource() {
        if (null == this.dataSource) {
            this.dataSource = SessionFactory.DATASOURCE_THREADLOCAL.get();
        }
        return this.dataSource;
    }

    public Connection getConnection() throws SQLException {
        try {
            Trans trans = Trans.current();
            if (null == trans) {
                return this.getDataSource().getConnection();
            } else {
                return trans.getConnection(this.getDataSource());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public boolean supportsAggressiveRelease() {
        return false;
    }

    public void configure(Properties properties) throws HibernateException {}

    public void close() throws HibernateException {}
}