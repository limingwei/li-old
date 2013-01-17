package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_bill")
public class Bill extends Record<Bill> {
    private static final long serialVersionUID = -3885011391270927891L;
}