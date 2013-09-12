package li.hibernate;

import li.annotation.Inject;
import li.hibernate.test.User;
import li.hibernate.test.UserDao;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnit4LiHibernate.class)
public class UserDaoTest {
    @Inject
    UserDao userDao;

    @Test
    public void find() {
        User user = userDao.find(1);
        System.out.println(user);
        System.out.println(user.getRoles());
    }

    @Test
    public void save() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("uuuuuuuuuu-" + i);
            user.setPassword("pppppppppppp-" + i);
            user.setTel("ttttttt-" + i);
            user.setFlag(1);
            userDao.save(user);
        }
    }

    @Test
    public void list() {
        System.err.println(userDao.list(null));
    }
}
