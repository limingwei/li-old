package li.people.record;

import java.util.ArrayList;
import java.util.List;

import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Table;
import li.dao.Record;
import li.people.Const;

@Bean
@Table("t_role")
public class Role extends Record<Role> implements Const {
    private static final long serialVersionUID = -3309607180685180059L;

    @Inject
    RoleResource roleResourceDao;

    public Role find(Integer id) {
        String sql = "SELECT resource_id FROM r_role_resource WHERE role_id = ?";
        Role role = super.find(id);
        if (null != role) {
            List<Role> roleResources = list(MAX_PAGE, sql, id);
            List<Integer> resourceIds = new ArrayList<>();
            for (Role resource : roleResources) {
                resourceIds.add(resource.get(Integer.class, "resource_id"));
            }
            role.set("resourceIds", resourceIds);
        }
        return role;
    }

    public Boolean update(Role role, Integer[] resourceIds) {
        return updateIgnoreNull(role) && roleResourceDao.reSave(role.get(Integer.class, "id"), resourceIds);
    }

    public Boolean save(Role role, Integer[] resourceIds) {
        return saveIgnoreNull(role) && roleResourceDao.reSave(role.get(Integer.class, "id"), resourceIds);
    }
}