package li.hibernate;

import java.io.File;
import java.util.List;

import javax.sql.DataSource;

import li.util.Files;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 * @author 明伟
 */
public class SessionFactory extends SessionFactoryWrapper {
    private static final long serialVersionUID = -8786008904930067379L;

    private org.hibernate.SessionFactory sessionFactory;

    private Configuration configuration;

    static final ThreadLocal<DataSource> DATASOURCE_THREADLOCAL = new ThreadLocal<DataSource>();

    public void setDataSource(DataSource dataSource) {
        DATASOURCE_THREADLOCAL.set(dataSource);
    }

    public DataSource getDataSource() {
        return DATASOURCE_THREADLOCAL.get();
    }

    public Configuration getConfiguration() {
        if (null == this.configuration) {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.provider_class", DataSourceConnectionProvider.class.getName());

            File root = Files.root();
            List<String> cfgs = Files.list(root, "^.*hibernate\\.cfg\\.xml$", true, 1);
            if (null != cfgs && 1 == cfgs.size()) {
                configuration.configure(new File(cfgs.get(0)));
            }

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

    public Session getCurrentSession() throws HibernateException {
        // Session session = OpenSessionInViewFilter.SESSION_THREADLOCAL.get();
        // return session;
        return super.getCurrentSession();
    }
}