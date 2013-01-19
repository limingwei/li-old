package li.people.record;

import java.util.ArrayList;
import java.util.List;

import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Table;
import li.annotation.Trans;
import li.dao.Record;
import li.people.Const;

@Bean
@Table("t_role")
public class Role extends Record<Role> implements Const {
    private static final long serialVersionUID = -3309607180685180059L;

    @Inject
    RoleResource roleResourceDao;

    public Role find(Integer id) {
        Role role = super.find(id);
        if (null != role) {
            List<RoleResource> roleResources = roleResourceDao.listByRoleId(id);
            List<Integer> resourceIds = new ArrayList<>();
            for (RoleResource roleResource : roleResources) {
                resourceIds.add(roleResource.get(Integer.class, "resource_id"));
            }
            role.set("resourceIds", resourceIds);
        }
        return role;
    }

    @Trans
    public Boolean update(Role role, Integer[] resourceIds) {
        return updateIgnoreNull(role) && roleResourceDao.reSave(role.get(Integer.class, "id"), resourceIds);
    }

    @Trans
    public Boolean save(Role role, Integer[] resourceIds) {
        return saveIgnoreNull(role) && roleResourceDao.reSave(role.get(Integer.class, "id"), resourceIds);
    }
}