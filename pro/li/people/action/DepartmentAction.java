package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Department;

@Bean
public class DepartmentAction extends AbstractAction implements Const {
    @Inject
    Department departmentDao;

    @At("department_list.do")
    public void list(Page page) {
        setRequest(LIST, departmentDao.list(page));
        setRequest(PAGE, page);
        view("department/list");
    }

    @At("department_add.do")
    public void add() {
        view("department/add");
    }

    @At(value = "department_save.do", method = POST)
    public void save(Department department) {
        write(departmentDao.saveIgnoreNull(department) ? "添加部门成功" : "添加部门失败");
    }

    @At("department_edit.do")
    public void edit(Integer id) {
        setRequest("secretary", departmentDao.find(id));
        view("department/edit");
    }

    @At(value = "department_update.do", method = POST)
    public void update(Department department) {
        write(departmentDao.updateIgnoreNull(department) ? "修改部门成功" : "修改部门失败");
    }

    @At(value = "department_delete.do", method = POST)
    public void delete(Integer id) {
        write(departmentDao.delete(id) ? "删除部门成功" : "删除部门失败");
    }
}