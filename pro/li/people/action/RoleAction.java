package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Role;

@Bean
public class RoleAction extends AbstractAction implements Const {
    @Inject
    Role roleDao;

    @At("role_list.do")
    public void list(Page page) {
        setRequest(LIST, roleDao.list(page));
        setRequest(PAGE, page);
        view("role/list");
    }
}