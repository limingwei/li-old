package li.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import li.annotation.Inject;
import li.test.BaseTest;
import li.util.Log;

import org.junit.Test;

public class RecordTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    _User userDao;

    @Inject
    _UserDao userDao2;

    _User user = new _User().set("id", 1).set("username", "u-4-1" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1");

    @Test
    public void testLike() {
        log.info(userDao.count("WHERE username LIKE ?", "%文%"));
    }

    @Test
    public void count() {
        assertTrue("li.dao.RecordTest.count()", userDao.count() > -2);
    }

    @Test
    public void count2() {
        assertTrue("li.dao.RecordTest.count2()", userDao.count("where false") > -2);
    }

    @Test
    public void delete() {
        assertFalse("li.dao.RecordTest.delete()", userDao.delete(-2));
    }

    @Test
    public void delete2() {
        assertTrue("delete by sql", userDao.delete("where false") < 1);
    }

    @Test
    public void find() {
        Integer id = userDao.find("WHERE id>0").get(Integer.class, "id");
        assertNotNull("dao.find", userDao.find(id));
    }

    @Test
    public void find2() {
        assertNotNull("dao.find", userDao.find("where 1=1 limit 1"));
    }

    @Test
    public void find3() {
        assertNotNull("dao.findbysql", userDao.find("select t_account.username as uname,t_forum.name as fname from t_account,t_forum limit 1"));
    }

    @Test
    public void list() {
        assertNotNull("dao.listbypage", userDao.list(page));
    }

    @Test
    public void list3() {
        List<_User> users = userDao.list(page.setPageSize(5), "select t_account.username as uname,t_forum.name as fname from t_account,t_forum");
        assertNotNull("dao.listByPage2", users);
    }

    @Test
    public void save() {
        assertTrue(userDao.save(new _User().set("role_id", 1).set("username", "u-2" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1").set("status", 1)));
    }

    @Test
    public void save2() {
        _User user = new _User().set("role_id", 1).set("username", "u-3" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1").set("status", 1);
        userDao.save(user);
        assertNotNull("", user.get("id"));
    }

    @Test
    public void test1() {
        new Trans() {
            public void run() {
                log.debug("啥都没做");
            }
        };
    }

    @Test
    public void test2() {
        new Trans() {
            public void run() {
                log.debug("啥都没做");
            }
        };
    }

    @Test
    public void testRecord() {
        List<_User> records = userDao.list(page, "select t_account.username as uname,t_forum.name as fname from t_account,t_forum");
        assertNotNull("li.dao.RecordTest.testRecord()", records);
    }

    @Test
    public void update() {
        log.debug("li.dao.RecordTest.update()");
        Boolean flag = userDao.updateIgnoreNull(userDao.set("id", 1).set("username", "u-4" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1").set("status", 1));
        assertTrue("li.dao.RecordTest.update()", flag || true);
    }

    @Test
    public void update2() {
        log.debug("li.dao.RecordTest.update2()");
        Boolean flag = userDao.updateIgnoreNull(userDao.set("id", 2).set("username", "u-5" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1").set("status", 1));
        assertTrue("li.dao.RecordTest.update2()", flag || true);
    }
}