package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_server")
public class Server extends Record<Server, Integer> {
    private static final long serialVersionUID = -3575427168553323744L;
}