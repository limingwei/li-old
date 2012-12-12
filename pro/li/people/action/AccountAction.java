package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.mvc.AbstractAction;
import li.people.record.Account;

@Bean
public class AccountAction extends AbstractAction {
    @Inject
    Account accountDao;

    @At(value = "login.do", method = GET)
    public void login() {
        view("account/login");
    }

    @At(value = "login.do", method = POST)
    public void login(Account account) {
        if (accountDao.login(account)) {
            view("");
        } else {
            redirect("index.do");
        }
    }

    @At(value = "register.do", method = GET)
    public void register() {
        view("account/register");
    }

    @At(value = "register.do", method = POST)
    public void register(Account account) {

    }
}