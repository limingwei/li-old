package li.people.action;

import li.annotation.Arg;
import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Account;
import li.util.Convert;

@Bean
public class AccountAction extends AbstractAction{
    @Inject
    Account accountDao;

    @At("account_list.do")
    public void list(Page page, String key) {
        setRequest(LIST, accountDao.list(key, page));
        setRequest(PAGE, page);
        keepParams("key");
        view("account/list");
    }

    @At("account_edit.do")
    public void edit(Integer id) {
        setRequest("account", accountDao.find(id));
        view("account/edit");
    }

    @At(value = "account_update.do", method = POST)
    public void update(Account account) {
        write(accountDao.updateIgnoreNull(account.md5PasswordIfNotNull()) ? "更新用户成功" : "更新用户失败");
    }

    @At("account_add.do")
    public void add() {
        setRequest("roles", roleDao.list(MAX_PAGE));
        view("account/add");
    }

    @At(value = "account_save.do", method = POST)
    public void save(Account account) {
        write(accountDao.saveIgnoreNull(account.md5PasswordIfNotNull()) ? "添加用户成功" : "添加用户失败");
    }

    @At(value = "account_delete.do", method = POST)
    public void delete(Integer id) {
        write(accountDao.delete(id) ? "删除用户成功" : "删除用户失败");
    }
}