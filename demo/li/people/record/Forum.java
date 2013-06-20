package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_forum")
public class Forum extends Record<Forum> {
    private static final long serialVersionUID = 4539272062271209117L;
}