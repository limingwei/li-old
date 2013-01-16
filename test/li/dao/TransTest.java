package li.dao;

import li.annotation.Inject;
import li.test.BaseTest;
import li.util.Convert;

import org.junit.Test;

import demo.record.Account;

public class TransTest extends BaseTest {
    @Inject
    Account accountDao;

    @Inject
    _User userDao;

    @Test
    public void test1() {
        System.err.println(new Trans(Convert.toMap("inpar", "inpar---", "1", "2", "3", "4")) {
            public void run() {
                userDao.updateIgnoreNull(new _User().set("id", 2).set("username", "u-1" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1").set("status", 1));
                userDao.updateIgnoreNull(new _User().set("id", 2).set("username", "u-2" + System.currentTimeMillis()).set("password", "p-2").set("email", "e-2").set("status", 1));
                map().put("outpar", "outpar---");
                System.err.println(map().get("inpar"));
            }
        }.map().get("outpar"));
    }

    @Test
    public void testMultipleTrans() {
        userDao.testMultipleTrans2();
    }

    @Test
    public void testPassValue() {
        _User user = (_User) new Trans(Convert.toMap("email", "tom@w.cn", "username", "xiaoming")) {
            public void run() {
                userDao.update("SET email=#email WHERE username=#username", map());
                map().put("user", userDao.find("WHERE username!=?", map().get("username")));
            }
        }.map().get("user");
        System.err.println(user);

        Boolean flag = new Trans() {
            public void run() {
                userDao.update("SET email='li@w.cn' WHERE 1=2", map());
            }
        }.success();
        System.err.println(flag);
    }

    @Test
    public void testTrans() {
        new Trans() {
            public void run() {
                System.err.println("trans 1 start");
                accountDao.list(null);
                new Trans() {
                    public void run() {
                        System.err.println("trans 2 start");
                        accountDao.list(null);
                        new Trans() {
                            public void run() {
                                System.err.println("trans 3 start");
                                accountDao.list(null);
                                System.err.println("trans 3 end");
                            }
                        };
                        accountDao.list(null);
                        System.err.println("trans 2 end");
                    }
                };
                accountDao.list(null);
                System.err.println("trans 1 end");
            }
        };
    }
}