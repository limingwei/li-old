package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Secretary;

@Bean
public class SecretaryAction extends AbstractAction implements Const {
    @Inject
    Secretary secretaryDao;

    @At("secretary_list.do")
    public void list(Page page) {
        setRequest(LIST, secretaryDao.list(page));
        setRequest(PAGE, page);
        view("secretary/list");
    }

    @At("secretary_add.do")
    public void add() {
        view("secretary/add");
    }

    @At(value = "secretary_save.do", method = POST)
    public void save(Secretary secretary) {
        write(secretaryDao.saveIgnoreNull(secretary) ? "添加干事成功" : "添加干事失败");
    }

    @At("secretary_edit.do")
    public void edit(Integer id) {
        setRequest("secretary", secretaryDao.find(id));
        view("secretary/edit");
    }

    @At(value = "secretary_update.do", method = POST)
    public void update(Secretary secretary) {
        write(secretaryDao.updateIgnoreNull(secretary) ? "修改干事成功" : "修改干事失败");
    }

    @At(value = "secretary_delete.do", method = POST)
    public void delete(Integer id) {
        write(secretaryDao.delete(id) ? "删除干事成功" : "删除干事失败");
    }
}
