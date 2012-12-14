package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.more.Convert;
import li.mvc.AbstractAction;
import li.people.record.Account;
import li.people.record.Permission;

@Bean
public class AccountAction extends AbstractAction {
    @Inject
    Account accountDao;

    @Inject
    Permission permissionDao;

    @At(value = "login.do", method = GET)
    public void login() {
        view("account/login");
    }

    @At(value = "login.do", method = POST)
    public void login(Account account) {
        if (null != accountDao.login(account)) {
            setSession("account", account);
            setSession("permissions", permissionDao.listByRoleId(Convert.toType(Integer.class, account.get("roleId"))));
            view("index");
        } else {
            redirect("index.do");
        }
    }

    @At("logout.do")
    public void logout() {
        removeSession("account");
        removeSession("permissions");
        redirect("index.do");
    }

    @At(value = "register.do", method = GET)
    public void register() {
        view("account/register");
    }

    @At(value = "register.do", method = POST)
    public void register(Account account) {

    }
}