package li.dao;

import java.util.Map;

import li.annotation.Inject;
import li.demo.dao.IUserDao;
import li.demo.record.Account;
import li.test.BaseTest;
import li.util.Convert;
import li.util.ConvertUtil;
import li.util.Log;

import org.junit.Test;

public class AbstractDaoTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    Account dao;

    @Inject
    _UserDao userDao;

    @Inject
    IUserDao userDaoImpl;

    @Test
    public void testDao() {
        log.info(ConvertUtil.toJson(userDaoImpl.list(null)));
    }

    @Test
    public void testFindByEmail() {
        log.info(ConvertUtil.toJson(userDaoImpl.findByEmail("")));
    }

    @Test
    public void testFindByUsername() {
        log.info(ConvertUtil.toJson(userDaoImpl.findByUsername("")));
    }

    @Test
    public void testQuery() {
        for (Record record : userDao.query(null, "select * from t_account")) {
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
        Map<?, ?> map = Convert.toMap("uname", "li-12345", "eml", "li@w.cn");
        log.debug(dao.list(page, "WHERE username=#uname OR email=#eml", map));

        log.debug(dao.find("WHERE username=? OR email=?", "li", "li@w.cn"));

        log.debug(dao.delete("WHERE username=#uname OR email=#eml OR id=?", map, 1));
    }
}
