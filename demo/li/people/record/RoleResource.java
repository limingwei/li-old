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

    public List<RoleResource> listByRoleId(Integer roleId) {
        return list(MAX_PAGE, "WHERE role_id = ?", roleId);
    }

    public Integer deleteByRoleId(Integer roleId) {
        return delete("WHERE role_id=?", roleId);
    }

    @Trans
    public Boolean reSave(Integer roleId, Integer[] resourceIds) {
        Boolean flag = 0 < delete("WHERE role_id=?", roleId);
        for (int i = 0; null != resourceIds && i < resourceIds.length; i++) {
            flag = save(new RoleResource().set("role_id", roleId).set("resource_id", resourceIds[i]));
        }
        return flag;
    }
}