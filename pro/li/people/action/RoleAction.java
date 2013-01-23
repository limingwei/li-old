package li.people.action;

import li.annotation.Arg;
import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Resource;
import li.people.record.Role;

@Bean
public class RoleAction extends AbstractAction implements Const {
    @Inject
    Role roleDao;

    @Inject
    Resource resourceDao;

    @At("role_list.do")
    public void list(Page page,String key) {
        setRequest(LIST, roleDao.list(page,key));
        setRequest(PAGE, page);
        passParams("key");
        view("role/list");
    }

    @At("role_edit.do")
    public void edit(Integer id) {
        Role role = roleDao.find(id);
        setRequest("resource_have", resourceDao.listByRoleId(MAX_PAGE, role.get(Integer.class, "id")));
        setRequest("resource_not_have", resourceDao.listNotHaveByRoleId(MAX_PAGE, role.get(Integer.class, "id")));
        setRequest("role", role);
        view("role/edit");
    }

    @At(value = "role_update.do", method = POST)
    public void update(Role role, @Arg("resource_id") Integer[] resourceIds) {
        write(roleDao.update(role, resourceIds) ? "更新角色成功" : "更新角色失败");
    }

    @At("role_add.do")
    public void add() {
        setRequest("resource_not_have", resourceDao.list(MAX_PAGE));
        view("role/add");
    }

    @At(value = "role_save.do", method = POST)
    public void save(Role role, @Arg("resource_id") Integer[] resourceIds) {
        write(roleDao.save(role, resourceIds) ? "添加角色成功" : "添加角色失败");
    }

    @At(value = "role_delete.do", method = POST)
    public void delete(Integer id) {
        write(roleDao.delete(id) ? "删除角色成功" : "删除角色失败");
    }
}