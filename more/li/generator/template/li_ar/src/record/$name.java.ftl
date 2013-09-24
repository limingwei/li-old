package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Page;
import li.dao.Record;

@Bean
@Table("t_account")
public class Account extends Record<Account, Integer> {}