package li.people.record;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;
import li.people.Const;

@Bean
@Table("t_resource")
public class Resource extends Record<Resource> implements Const {
    private static final long serialVersionUID = 7989017780707672816L;

    public List<Resource> listByRoleId(Integer roleId) {
        String sql = "WHERE id IN (SELECT resource_id FROM r_role_resource WHERE role_id = ?)";
        return super.list(MAX_PAGE, sql, roleId);
    }
}