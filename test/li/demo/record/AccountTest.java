package li.demo.record;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.people.record.Account;
import li.test.BaseTest;
import li.util.Log;

import org.junit.Before;
import org.junit.Test;

public class AccountTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    Account account;

    @Before
    public void before() {
        assertNotNull(account);
    }

    @Test
    public void testList() {
        log.debug("li.demo.record.AccountTest.testList()");
    }
}