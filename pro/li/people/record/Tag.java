package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_tag")
public class Tag extends Record<Tag> {
    private static final long serialVersionUID = -1479556790280813012L;
}