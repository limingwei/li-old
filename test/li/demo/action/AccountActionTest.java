package li.demo.action;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.people.action.AccountAction;
import li.people.record.Account;
import li.test.ActionTest;

import org.junit.Before;
import org.junit.Test;

public class AccountActionTest extends ActionTest {
    @Inject
    AccountAction accountAction;

    private Account account = new Account();

    @Before
    public void before() {
        assertNotNull(accountAction);
        account.set("id", "1").set("username", "uname").set("password", "pwd").set("email", "eml-" + System.currentTimeMillis()).set("flag", 1);
    }

    @Test
    public void delete() {
        accountAction.delete(1);
    }

    @Test
    public void list() {
        accountAction.list(page, "文");
    }

    @Test
    public void loginView() {
        accountAction.login();
    }

    @Test
    public void login() {
        accountAction.login(account);
    }

    @Test
    public void logout() {
        accountAction.logout();
    }

    @Test
    public void save() {
        account.remove("id");
        accountAction.save(account.set("role_id", 1).set("username", System.currentTimeMillis()));
    }

    @Test
    public void signupView() {
        accountAction.register();
    }

    @Test
    public void signup() {
        accountAction.register(account);
    }

    @Test
    public void update() {
        accountAction.update(account);
    }
}