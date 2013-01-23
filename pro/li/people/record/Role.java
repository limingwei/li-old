package li.people.record;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Table;
import li.annotation.Trans;
import li.dao.Page;
import li.dao.Record;
import li.people.Const;

@Bean
@Table("t_role")
public class Role extends Record<Role> implements Const {
    private static final long serialVersionUID = -3309607180685180059L;

    @Inject
    RoleResource roleResourceDao;

    @Inject
    Resource resourceDao;

    @Trans
    public List<Role> list(Page page) {
        List<Role> roles = super.list(page);
        for (Role role : roles) {
            role.set("resources", resourceDao.listByRoleId(MAX_PAGE, role.get(Integer.class, "id")));
        }
        return roles;
    }

    @Trans
    public Boolean delete(Number id) {
        return super.delete(id) | -1 < roleResourceDao.deleteByRoleId((Integer) id);
    }

    @Trans
    public Boolean update(Role role, Integer[] resourceIds) {
        return updateIgnoreNull(role) | roleResourceDao.reSave(role.get(Integer.class, "id"), resourceIds);
    }

    @Trans
    public Boolean save(Role role, Integer[] resourceIds) {
        return saveIgnoreNull(role) | roleResourceDao.reSave(role.get(Integer.class, "id"), resourceIds);
    }
}