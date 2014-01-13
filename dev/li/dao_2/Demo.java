package li.dao_2;

import li.annotation.Inject;
import li.dao.Page;
import li.test.BaseTest;

import org.junit.Test;

public class Demo extends BaseTest {
    @Inject
    UserDao userDao;

    @Test
    public void listBySql() {
        System.err.println(userDao.list("SELECT * FROM t_user WHERE id>? LIMIT ?,?", 1, Page.MAX.getFrom(), Page.MAX.getPageSize()));
    }

    @Test
    public void list() {
        System.err.println(userDao.list(Page.MAX));
    }

    @Test
    public void find() {
        System.err.println(userDao.find(1));
    }

    @Test
    public void findBySql() {
        System.err.println(userDao.find("SELECT * FROM t_user WHERE id=?", 1));
    }
}