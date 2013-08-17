package li.dao;

import li.annotation.Inject;
import li.dao.test._User;
import li.people.record.Account;
import li.test.BaseTest;
import li.util.Convert;
import li.util.Log;

import org.junit.Test;

public class TransTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    Account accountDao;

    @Inject
    _User userDao;

    // @Test
    public void rollback() {
        new Trans() {
            public void run() {
                userDao.update("SET flag='a' WHERE true");
                userDao.update("SET flag='a' WHERE true");
            }
        };
    }

    // @Test
    public void norollback() {
        userDao.update("SET flag='a' WHERE true");
        userDao.update("SET flag='a' WHERE true");
    }

    @Test
    public void test1() {
        log.debug(new Trans(Convert.toMap("inpar", "inpar---", "1", "2", "3", "4")) {
            public void run() {
                userDao.updateIgnoreNull(new _User().set("id", 2).set("username", "u-1" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1").set("status", 1));
                userDao.updateIgnoreNull(new _User().set("id", 2).set("username", "u-2" + System.currentTimeMillis()).set("password", "p-2").set("email", "e-2").set("status", 1));
                map().put("outpar", "outpar---");
                log.debug(map().get("inpar"));
            }
        }.map().get("outpar"));
    }

    @Test
    public void testMultipleTrans() {
        log.debug("li.dao._User.testMultipleTrans2()");
        userDao.testMultipleTrans2();
    }

    @Test
    public void testPassValue() {
        _User user = (_User) new Trans(Convert.toMap("email", "tom@w.cn", "username", "xiaoming")) {
            public void run() {
                userDao.update("SET email=#email WHERE username=#username", map());
                map().put("user", userDao.find("WHERE username!=? LIMIT 1", map().get("username")));
            }
        }.map().get("user");

        log.debug(user);

        Boolean flag = new Trans() {
            public void run() {
                userDao.update("SET email='li@w.cn' WHERE 1=2", map());
            }
        }.success();

        log.debug(flag);
    }

    @Test
    public void testTrans() {
        new Trans() {
            public void run() {
                log.debug("trans 1 start");
                accountDao.list(page.setPageSize(5));
                new Trans() {
                    public void run() {
                        log.debug("trans 2 start");
                        accountDao.list(page.setPageSize(5));
                        new Trans() {
                            public void run() {
                                log.debug("trans 3 start");
                                accountDao.list(page.setPageSize(5));
                                log.debug("trans 3 end");
                            }
                        };
                        accountDao.list(page.setPageSize(5));
                        log.debug("trans 2 end");
                    }
                };
                accountDao.list(page.setPageSize(5));
                log.debug("trans 1 end");
            }
        };
    }
}