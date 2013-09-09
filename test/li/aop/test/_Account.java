package li.aop.test;

import java.util.List;

import javax.sql.DataSource;

import li.annotation.Aop;
import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Table;
import li.annotation.Trans;
import li.dao.Page;
import li.dao.QueryBuilder;
import li.dao.Record;

@Bean
@Table("t_account")
public class _Account extends Record<_Account, Integer> {
    private static final long serialVersionUID = -3592765768245992120L;

    @Inject("li2")
    DataSource dataSource;

    @Inject
    QueryBuilder queryBuilder;

    @Trans
    @Aop(_LogFilter.class)
    public List<_Account> list(Page page) {
        return super.list(page);
    }

    @Trans(readOnly = false)
    @Aop(_LogFilter.class)
    public void testUpdate() {
        System.err.println(super.list(null));
        super.update("SET flag=1 WHERE id=1");
    }
}