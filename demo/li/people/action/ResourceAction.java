package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Resource;

@Bean
public class ResourceAction extends AbstractAction implements Const {
    @Inject
    Resource resourceDao;

    @At("resource_list.do")
    public void list(Page page, String key) {
        setRequest(LIST, resourceDao.list(page, key));
        setRequest(PAGE, page);
        keepParams("key");
        view("resource/list");
    }

    @At("resource_edit.do")
    public void edit(Integer id) {
        setRequest("resource", resourceDao.find(id));
        view("resource/edit");
    }

    @At(value = "resource_update.do", method = POST)
    public void update(Resource resource) {
        write(resourceDao.updateIgnoreNull(resource) ? "更新资源成功" : "更新资源失败");
    }

    @At("resource_add.do")
    public void add() {
        view("resource/add");
    }

    @At(value = "resource_save.do", method = POST)
    public void save(Resource resource) {
        write(resourceDao.saveIgnoreNull(resource) ? "添加资源成功" : "添加资源失败");
    }

    @At(value = "resource_delete.do", method = POST)
    public void delete(Integer id) {
        write(resourceDao.delete(id) ? "删除资源成功" : "删除资源失败");
    }
}