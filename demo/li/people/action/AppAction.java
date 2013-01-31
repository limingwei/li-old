package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.App;

@Bean
public class AppAction extends AbstractAction implements Const {
    @Inject
    App appDao;

    @At("app_list.do")
    public void list(Page page) {
        setRequest(LIST, appDao.list(page));
        setRequest(PAGE, page);
        view("app/list");
    }

    @At("app_add.do")
    public void add() {
        view("app/add");
    }

    @At(value = "app_save.do", method = POST)
    public void save(App app) {
        write(appDao.saveIgnoreNull(app) ? "添加应用成功" : "添加应用失败");
    }

    @At("app_edit.do")
    public void edit(Integer id) {
        setRequest("app", appDao.find(id));
        view("app/edit");
    }

    @At(value = "app_update.do", method = POST)
    public void update(App app) {
        write(appDao.updateIgnoreNull(app) ? "修改应用成功" : "修改应用失败");
    }

    @At(value = "app_delete.do", method = POST)
    public void delete(Integer id) {
        write(appDao.delete(id) ? "删除应用成功" : "删除应用失败");
    }
}