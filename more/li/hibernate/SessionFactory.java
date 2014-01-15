package li.hibernate;

import java.io.File;
import java.util.List;

import javax.sql.DataSource;

import li.util.Files;

import org.hibernate.cfg.Configuration;

/**
 * SessionFactory
 * 
 * @author 明伟
 */
public class SessionFactory extends SessionFactoryWrapper {
    private static final long serialVersionUID = -8786008904930067379L;

    static final ThreadLocal<DataSource> DATASOURCE_THREADLOCAL = new ThreadLocal<DataSource>();

    private Configuration configuration;

    private org.hibernate.SessionFactory sessionFactory;

    public void setDataSource(DataSource dataSource) {
        DATASOURCE_THREADLOCAL.set(dataSource);
    }

    public DataSource getDataSource() {
        return DATASOURCE_THREADLOCAL.get();
    }

    /**
     * read hibernate configuration
     */
    public Configuration getConfiguration() {
        if (null == this.configuration) {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.provider_class", "li.hibernate.DataSourceConnectionProvider");
            if (new File(Files.root() + File.separator + "hibernate.cfg.xml").exists()) {
                configuration.configure();// 读取 hibernate.cfg.xml
            }
            File root = Files.root();
            List<String> hbms = Files.list(root, "^.*\\.hbm\\.xml$", true, 1);
            for (String hbm : hbms) {
                configuration.addResource(hbm.replace(root + "", ""));
            }
            this.configuration = configuration;
        }
        return this.configuration;
    }

    /**
     * buildSessionFactory
     */
    public org.hibernate.SessionFactory getSessionFactory() {
        if (null == this.sessionFactory) {
            this.sessionFactory = this.getConfiguration().buildSessionFactory();
        }
        return this.sessionFactory;
    }
}