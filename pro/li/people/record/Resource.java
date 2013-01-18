package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;
import li.people.Const;

@Bean
@Table("t_resource")
public class Resource extends Record<Resource> implements Const {
    private static final long serialVersionUID = 7989017780707672816L;
}