package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.People;

@Bean
public class PeopleAction extends AbstractAction implements Const {
    @Inject
    People peopleDao;

    @At("people_list.do")
    public void list(Page page) {
        setRequest(LIST, peopleDao.list(page));
        setRequest(PAGE, page);
        view("people/list");
    }

    @At("people_edit.do")
    public void edit(Integer id) {
        view("people/edit");
    }

    @At(value = "people_update.do", method = POST)
    public void update(People people) {
        write(peopleDao.updateIgnoreNull(people) ? "修改会员成功" : "修改会员失败");
    }

    @At("people_add.do")
    public void add() {
        view("people/add");
    }

    @At(value = "people_save.do", method = POST)
    public void save(People people) {
        write(peopleDao.saveIgnoreNull(people) ? "新增会员成功" : "新增会员失败");
    }

    @At(value = "people_delete.do", method = POST)
    public void delete(Integer id) {
        write(peopleDao.delete(id) ? "删除会员成功" : "删除会员失败");
    }
}