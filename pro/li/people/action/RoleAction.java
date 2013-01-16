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

    @At("role_edit.do")
    public void edit(Integer id) {
        setRequest("role", roleDao.find(id));
        view("role/edit");
    }

    @At(value = "role_update.do", method = POST)
    public void update(Role role) {
        write(roleDao.updateIgnoreNull(role) ? "更新角色成功" : "更新角色失败");
    }

    @At("role_add.do")
    public void add() {
        view("role/add");
    }

    @At(value = "role_save.do", method = POST)
    public void save(Role role) {
        write(roleDao.saveIgnoreNull(role) ? "添加角色成功" : "添加角色失败");
    }

    @At(value = "role_delete.do", method = POST)
    public void delete(Integer id) {
        write(roleDao.delete(id) ? "删除角色成功" : "删除角色失败");
    }
}