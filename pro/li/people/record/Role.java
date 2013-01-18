package li.people.record;

import java.util.ArrayList;
import java.util.List;

import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_role")
public class Role extends Record<Role> {
    private static final long serialVersionUID = -3309607180685180059L;

    @Inject
    RoleResource roleResourceDao;

    @Inject
    Resource resourceDao;

    public Role find(Integer id) {
        Role role = super.find(id);
        if (null != role) {
            List<Resource> resources = resourceDao.listByRoleId(id);
            List<Integer> resourceIds = new ArrayList<>();
            for (Resource resource : resources) {
                resourceIds.add(resource.get(Integer.class, "id"));
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