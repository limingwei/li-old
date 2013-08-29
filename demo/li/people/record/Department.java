package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_department")
public class Department extends Record<Department, Integer> {
    private static final long serialVersionUID = -2608754699704926062L;
}