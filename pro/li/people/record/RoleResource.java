package li.people.record;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Table;
import li.annotation.Trans;
import li.dao.Record;
import li.people.Const;

@Bean
@Table("r_role_resource")
public class RoleResource extends Record<RoleResource> implements Const {
    private static final long serialVersionUID = 6598324284655928582L;

    @Trans
    public Boolean reSave(Integer roleId, Integer[] resourceIds) {
        Boolean flag = 0 < delete("WHERE role_id=?", roleId);
        for (Integer resourceId : resourceIds) {
            flag = save(new RoleResource().set("role_id", roleId).set("resource_id", resourceId));
        }
        return flag;
    }

    public List<RoleResource> listByRoleId(Integer roleId) {
        String sql = "SELECT resource_id FROM r_role_resource WHERE role_id = ?";
        return list(MAX_PAGE, sql, roleId);
    }
}