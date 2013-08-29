package li.dao.test;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_account")
public class StringIdRecord extends Record<StringIdRecord, String> {
    private static final long serialVersionUID = -6532807889921537703L;
}