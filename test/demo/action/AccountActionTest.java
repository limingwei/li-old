package demo.action;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.test.ActionTest;

import org.junit.Before;
import org.junit.Test;

import demo.record.Account;

public class AccountActionTest extends ActionTest {
    @Inject
    AccountAction accountAction;
    private Account account = new Account();

    @Before
    public void before() {
        assertNotNull(accountAction);
        account.set("id", "1").set("username", "uname").set("password", "pwd").set("email", "eml").set("status", 1);
    }

    @Test
    public void add() {
        accountAction.add();
    }

    @Test
    public void delete() {
        accountAction.delete(1);
    }

    @Test
    public void edit() {
        accountAction.edit(1);
    }

    @Test
    public void list() {
        accountAction.list(page);
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
        accountAction.save(account.set("role_id", 1).set("username", System.currentTimeMillis()));
    }

    @Test
    public void signupView() {
        accountAction.signup();
    }

    @Test
    public void signup() {
        accountAction.signup(account);
    }

    @Test
    public void update() {
        accountAction.update(account);
    }
}