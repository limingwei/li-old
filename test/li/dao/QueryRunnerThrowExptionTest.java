package li.dao;

import li.annotation.Inject;
import li.people.record.Account;
import li.test.BaseTest;

import org.junit.Test;

public class QueryRunnerThrowExptionTest extends BaseTest {
    @Inject
    Account accountDao;

    @Test
    public void testQueryRunnerThrowExption() {
        new Trans() {
            public void run() {
                accountDao.update("UPDATE t_account SET flag='error' WHERE id=2");
            }
        };
    }

    @Test
    public void testQueryRunnerThrowExption2() {
        accountDao.update("UPDATE t_account SET flag='error' WHERE id=2");
    }
}