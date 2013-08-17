package li.dao.test;

import li.annotation.Bean;
import li.dao.QueryBuilder;

@Bean
public class AnotherQueryBuilder extends QueryBuilder {
    public String countAll() {
        return "SELECT COUNT(*) FROM " + beanMeta.table;
    }
}