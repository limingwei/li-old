package li.hibernate;

import java.io.File;
import java.util.List;

import li.annotation.Bean;
import li.util.Files;

import org.hibernate.cfg.Configuration;

@Bean
public class SessionFactory extends SessionFactoryWrapper {
    private static final long serialVersionUID = -8786008904930067379L;

    private org.hibernate.SessionFactory sessionFactory;

    private Configuration configuration;

    public Configuration getConfiguration() {
        if (null == this.configuration) {
            Configuration configuration = new Configuration();
            configuration.configure();
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
}