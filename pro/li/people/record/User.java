package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_user")
public class User extends Record<User> {
    private static final long serialVersionUID = 4807570774011333657L;
}