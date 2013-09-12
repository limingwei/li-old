package li.hibernate.test;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.hibernate.AbstractDao;
import li.hibernate.SessionFactory;

@Bean
public class UserDao extends AbstractDao<User, Integer> {
    @Inject
    SessionFactory sessionFactory;

    public List<User> list(Page page) {
        return super.list(page);
    }

    public Boolean save(User entry) {
        return super.save(entry);
    }
}