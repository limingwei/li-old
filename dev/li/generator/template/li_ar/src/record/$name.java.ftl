package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Page;
import li.dao.Record;

@Bean
@Table("${tableName}")
public class ${entityName} extends Record<Account, Integer> {}