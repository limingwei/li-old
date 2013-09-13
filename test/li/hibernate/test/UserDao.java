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

    public Boolean save(User entry) {
        return super.save(entry);
    }

    @Trans(value = Connection.TRANSACTION_SERIALIZABLE, readOnly = false)
    public void testTrans() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("uuuuuuuuuu-" + i);
            user.setPassword("pppppppppppp-" + i);
            user.setTel("ttttttt-" + i);
            user.setFlag(1);
            super.save(user);
        }
    }
}