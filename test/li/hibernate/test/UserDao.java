package li.hibernate.test;

import java.sql.Connection;
import java.util.List;

import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Trans;
import li.dao.Page;
import li.hibernate.AbstractDao;
import li.hibernate.Daos;
import li.hibernate.SessionFactory;

@Bean
public class UserDao extends AbstractDao<User, Integer> {
    @Inject
    SessionFactory sessionFactory;

    public List<User> list(Page page) {
        return Daos.list(this.getSessionFactory(), User.class, page);
    }

    @Trans(value = Connection.TRANSACTION_SERIALIZABLE, readOnly = false)
    public Boolean save(User entry) {
        return super.save(entry);
    }
}