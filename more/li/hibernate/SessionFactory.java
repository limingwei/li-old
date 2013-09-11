package li.hibernate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import li.util.Files;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

public class SessionFactory extends SessionFactoryWrapper {
    private static final long serialVersionUID = -8786008904930067379L;

    private org.hibernate.SessionFactory sessionFactory;

    private Configuration configuration;

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration getConfiguration() {
        if (null == this.configuration) {
            Configuration configuration = new Configuration().configure();
            File root = Files.root();
            List<String> hbms = Files.list(root, "^.*\\.hbm\\.xml$", true, 1);
            for (String hbm : hbms) {
                configuration.addResource(hbm.replace(root + "", ""));
            }
            this.configuration = configuration;
        }
        return this.configuration;
    }

    public org.hibernate.SessionFactory getSessionFactory() {
        if (null == this.sessionFactory) {
            this.sessionFactory = this.getConfiguration().buildSessionFactory();
        }
        return this.sessionFactory;
    }

    public Session openSession() throws HibernateException {
        return null == this.dataSource ? super.openSession() : super.openSession(this.getConnection());
    }

    public Session openSession(Interceptor interceptor) throws HibernateException {
        return null == this.dataSource ? super.openSession(interceptor) : super.openSession(this.getConnection(), interceptor);
    }
}