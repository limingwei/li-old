package li.ioc;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.dao.test._User;
import li.dao.test._UserDao;
import li.ioc.test._AAAAA;
import li.test.BaseTest;

import org.junit.Test;

public class IocTest extends BaseTest {
    @Inject
    _AAAAA aaaa;

    @Inject
    _UserDao userDao;

    @Inject
    _User user;

    @Test
    public void getByName() {
        assertNotNull(Ioc.get("beanA"));
    }

    @Test
    public void getByTypeAndName() {
        assertNotNull(Ioc.get(_AAAAA.class, "beanA"));
    }

    @Test
    public void getByType() {
        assertNotNull(Ioc.get(_AAAAA.class));
    }

    @Test
    public void 通过Ioc来配置Dao() {
        System.out.println(userDao.getDataSource());
        System.out.println(userDao.getQueryBuilder());
        System.out.println(user.getDataSource());
        System.out.println(user.getQueryBuilder());
    }
}