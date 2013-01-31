package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_secretary")
public class Secretary extends Record<Secretary> {
    private static final long serialVersionUID = -7408263358873096083L;
}