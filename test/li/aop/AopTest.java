package li.aop;

import li.annotation.Inject;
import li.test.BaseTest;
import li.util.Log;

import org.junit.Test;

public class AopTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    _Account account;

    @Inject
    _User user;

    @Test
    public void testAop() {
        account.list(null);
    }

    @Test
    public void testAop2() {
        log.debug(user.sayHi("abc", "xyz"));
    }

    @Test
    public void testAop3() {
        user.test();
    }
}