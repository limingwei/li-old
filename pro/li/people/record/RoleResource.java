package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("r_role_resource")
public class RoleResource extends Record<RoleResource> {
    private static final long serialVersionUID = 6598324284655928582L;

    public Boolean reSave(Integer roleId, Integer[] resourceIds) {
        Boolean flag = 0 < delete("WHERE role_id=?", roleId);

        for (Integer resourceId : resourceIds) {
            flag = save(new RoleResource().set("role_id", roleId).set("resource_id", resourceId));
        }

        return flag;
    }
}