package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_post")
public class Post extends Record<Post> {

    /**
     * 
     */
    private static final long serialVersionUID = 604890678282981021L;

}
