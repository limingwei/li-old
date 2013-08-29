package li.dao;

import li.annotation.Inject;
import li.dao.test._UserDao;
import li.people.record.Account;
import li.test.BaseTest;
import li.util.Convert;
import li.util.Log;

import org.junit.Test;

public class AbstractDaoTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    Account dao;

    @Inject
    _UserDao userDao;

    @Test
    public void testQuery() {
        for (Record<?, ?> record : userDao.query(null, "SELECT * FROM t_account LIMIT 5")) {
            log.debug(record);
        }
    }

    @Test
    public void testCount() {
        log.debug(userDao.count());
    }

    @Test
    public void testCountBySql() {
        log.debug(userDao.count("SELECT * FROM t_account WHERE id>'1' LIMIT 2,3", new Object[] {}));
    }

    @Test
    public void test() {
        log.debug(dao.list(page.setPageSize(5), "WHERE username=#uname OR email=#eml", Convert.toMap("uname", "li-12345", "eml", "li@w.cn")));

        log.debug(dao.find("WHERE username=? OR email=?", "li", "li@w.cn"));

        log.debug(dao.delete("WHERE username=#uname OR email=#eml OR id=?", Convert.toMap("uname", "li-12345", "eml", "li@w.cn"), 1));
    }
}
