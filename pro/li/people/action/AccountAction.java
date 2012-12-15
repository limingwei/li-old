package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.more.Convert;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Account;
import li.people.record.Role;

@Bean
public class AccountAction extends AbstractAction implements Const {
    @Inject
    Account accountDao;

    @Inject
    Role roleDao;

    @At("account_list.do")
    public void list(Page page) {
        setRequest(LIST, accountDao.list(page));
        setRequest(PAGE, page);
        view("account/list");
    }

    @At("account_edit_role.do")
    public void editRole(Integer id) {
        setRequest("account", accountDao.find(id));
        setRequest("roles", roleDao.list(new Page()));
        view("account/edit_role");
    }

    @At("account_update_role.do")
    public void updateRole(Account account) {
        write(accountDao.updateIgnoreNull(account) ? "更新角色成功" : "更新角色失败");
    }

    @At("account_edit_password.do")
    public void editPassword(Integer id) {
        setRequest("account", accountDao.find(id));
        view("account/edit_password");
    }

    @At(value = "login.do", method = GET)
    public void login() {
        view("account/login");
    }

    @At(value = "login.do", method = POST)
    public void login(Account account) {
        if (null != accountDao.login(account)) {
            setSession("account", account);
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
        if (null != accountDao.findByUsername(account.get(String.class, "username"))) {
            write("此用户名已注册");
        } else if (null != account.findByEmail(account.get(String.class, "email"))) {
            write("此邮箱已注册");
        } else if (accountDao.save(account.set("password", Convert.toMD5(account.get("password"))).set("status", "1"))) {
            login(account);
            write("注册成功");
        } else {
            write("注册失败");
        }
    }
}