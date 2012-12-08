package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.record.People;

@Bean
public class PeopleAction extends AbstractAction {
    @Inject
    People peopleDao;

    @At("people_list.do")
    public void list(Page page) {
        setRequest("list", peopleDao.list(page));
        setRequest("page", page);
        view("people/list");
    }

    @At("people_edit.do")
    public void edit(Integer id) {
        view("people/edit");
    }

    @At("hot.do")
    public void hot() {
        view("people/hot");
    }
}