package li.model;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import li.dao.test._User;
import li.ioc.Ioc;
import li.test.BaseTest;

import org.junit.Before;
import org.junit.Test;

public class BeanTest extends BaseTest {
    private DataSource dataSource;
    private Class<?> type;

    @Before
    public void before() {
        dataSource = Ioc.get(DataSource.class);
        type = _User.class;
    }

    @Test
    public void getMeta() {
        Bean bean = Bean.getMeta(dataSource, type);
        assertNotNull(bean);
    }
}