package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_role")
public class Role extends Record<Role> {
    private static final long serialVersionUID = -3309607180685180059L;
}