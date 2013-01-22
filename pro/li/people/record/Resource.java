package li.people.record;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Page;
import li.dao.Record;
import li.people.Const;

@Bean
@Table("t_resource")
public class Resource extends Record<Resource> implements Const {
    private static final long serialVersionUID = 7989017780707672816L;

    public List<Resource> listByRoleId(Integer roleId) {
        return list(MAX_PAGE, "WHERE id IN (SELECT resource_id from r_role_resource WHERE role_id=?) ORDER BY description ASC", roleId);
    }

    public List<Resource> list(Page page) {
        return super.list(page, "SELECT * FROM t_resource ORDER BY description ASC");
    }
}