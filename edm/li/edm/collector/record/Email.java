package li.edm.collector.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_email")
public class Email extends Record<Email> {
    private static final long serialVersionUID = 8298489756424064419L;
}