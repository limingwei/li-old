package li.demo.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_forum")
public class Forum extends Record<Forum> {
    private static final long serialVersionUID = -9092573987891880776L;
}