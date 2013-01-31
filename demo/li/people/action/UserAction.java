package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.User;

@Bean
public class UserAction extends AbstractAction implements Const {
    @Inject
    User userDao;

    @At("user_list.do")
    public void list(Page page) {
        setRequest(LIST, userDao.list(page));
        setRequest(PAGE, page);
        view("user/list");
    }
}