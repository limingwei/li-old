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
    public void save() {}

    @Test
    public void list() {
        System.err.println(userDao.list(null));
    }

    @Test
    public void testTrans() {
        userDao.testTrans();
    }
}
