package li.edm.sender;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_deal")
public class Deal extends Record<Deal> {
    private static final long serialVersionUID = 5716220914471495657L;
}