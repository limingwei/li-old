package li.dao.test;

import javax.sql.DataSource;

import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.AbstractDao;
import li.dao.QueryBuilder;

@Bean
public class _UserDao extends AbstractDao<_User> {
    @Inject("li2")
    DataSource dataSource;

    @Inject
    QueryBuilder queryBuilder;
}