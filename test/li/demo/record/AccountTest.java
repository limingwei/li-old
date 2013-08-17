package li.demo.record;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.people.record.Account;
import li.test.BaseTest;
import li.test.Rollback;
import li.util.Log;

import org.junit.Before;
import org.junit.Test;

@Rollback
public class AccountTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    Account accountDao;

    @Before
    public void before() {
        accountDao.count();
        assertNotNull(accountDao);
    }

    @Test
    public void testList() {
        log.debug("li.demo.record.AccountTest.testList()");
    }

    @Test
    public void testUpdate() {
        accountDao.update("SET id=1 WHERE id=1");
    }
}