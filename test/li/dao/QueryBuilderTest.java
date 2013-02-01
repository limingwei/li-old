package li.dao;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import javax.sql.DataSource;

import li.ioc.Ioc;
import li.model.Bean;
import li.test.BaseTest;
import li.util.Convert;
import li.util.Log;

import org.junit.Before;
import org.junit.Test;

import demo.model.User;

public class QueryBuilderTest extends BaseTest {
    private static final Log log = Log.init();

    DataSource dataSource = Ioc.get(DataSource.class);

    QueryBuilder queryBuilder = new QueryBuilder();

    User user = new User();

    @Before
    public void before() throws Exception {
        queryBuilder.dataSource = dataSource;
        queryBuilder.beanMeta = Bean.getMeta(dataSource, User.class);

        user.setId(1);
        user.setUsername("username-1");
        user.setPassword("password-1");
        user.setEmail("email-1");
    }

    @Test
    public void testWrap() {
        Object null_arg = null;

        Double double_arg = 1.2;
        double double_arg_2 = 1.22;

        Float float_arg = 1.23F;
        float float_arg_2 = 1.232F;

        Integer integer_arg = 1234;
        int int_arg_2 = 12342;

        Long long_arg = 12345L;
        long long_arg_2 = 123452L;

        Short short_arg = 12;
        short short_arg_2 = 122;

        Boolean boolean_arg = false;
        boolean bool_arg_2 = true;

        java.util.Date util_date_arg = new java.util.Date(System.currentTimeMillis());
        java.sql.Date sql_date_arg = new java.sql.Date(System.currentTimeMillis());
        java.sql.Time sql_time_arg = new java.sql.Time(System.currentTimeMillis());
        java.sql.Timestamp sql_timestamp_arg = new java.sql.Timestamp(System.currentTimeMillis());

        String string_arg = "字符串参数";

        Object[] args = { null_arg, double_arg, double_arg_2, float_arg, float_arg_2, integer_arg, int_arg_2, long_arg, long_arg_2, short_arg, short_arg_2, boolean_arg, bool_arg_2, util_date_arg, sql_date_arg, sql_time_arg, sql_timestamp_arg, string_arg };
        for (Object arg : args) {
            log.info(queryBuilder.wrap(arg));
        }
    }

    @Test
    public void countAll() {
        assertEquals("SELECT COUNT(*) FROM t_account", queryBuilder.countAll());
    }

    @Test
    public void countBySql() {
        assertEquals("SELECT COUNT(*) FROM t_account WHERE id>'1'", queryBuilder.countBySql("WHERE id>?", new Object[] { "1" }));

        assertEquals("SELECT COUNT(1) FROM t_account", queryBuilder.countBySql("SELECT COUNT(1) FROM t_account", new Object[] {}));

        assertEquals("SELECT COUNT(id) FROM t_account", queryBuilder.countBySql("SELECT COUNT(id) FROM t_account", new Object[] {}));

        assertEquals("SELECT COUNT(DISTINCT id) FROM t_account", queryBuilder.countBySql("SELECT COUNT(DISTINCT id) FROM t_account", new Object[] {}));
    }

    @Test
    public void deleteBySql() {
        assertEquals("DELETE FROM t_account WHERE id='1'", queryBuilder.deleteBySql("WHERE id=?", new Object[] { "1" }));
    }

    @Test
    public void deleteById() {
        assertEquals("DELETE FROM t_account WHERE id=1", queryBuilder.deleteById(1));
    }

    @Test
    public void findById() {
        assertEquals("SELECT * FROM t_account WHERE id=123", queryBuilder.findById(123));
    }

    @Test
    public void list() {
        assertEquals("SELECT * FROM t_account LIMIT 0,10", queryBuilder.list(page));
    }

    @Test
    public void listBySql() {
        assertEquals("SELECT * FROM t_account WHERE id>'1' LIMIT 0,10", queryBuilder.listBySql(page, "WHERE id>?", new Object[] { "1" }));
    }

    @Test
    public void save() {
        assertEquals("INSERT INTO t_account (username,password,email) VALUES ('username-1','password-1','email-1')", queryBuilder.save(user));
    }

    @Test
    public void setArgMap() {
        String sql = "SELECT * FROM WHERE id=#id OR username LIKE #username";
        Map<Object, Object> map = Convert.toMap("id", 1, "username", "%li%");
        assertEquals("SELECT * FROM WHERE id=1 OR username LIKE '%li%'", queryBuilder.setArgMap(sql, map));
    }

    @Test
    public void setArgs() {
        String sql = "SELECT * FROM WHERE id=? OR username LIKE ?";
        Object[] args = { 1, "%li%" };
        assertEquals("SELECT * FROM WHERE id=1 OR username LIKE '%li%'", queryBuilder.setArgs(sql, args));
    }

    @Test
    public void setPage() {
        assertEquals("SELECT * FROM t_account LIMIT 0,10", queryBuilder.setPage("SELECT * FROM t_account", page));
    }

    @Test
    public void testSetArgs() {
        String sql = "SELECT * FROM t_account where username=?";
        Object[] args = { "uuu" };
        sql = queryBuilder.setArgs(sql, args);
        assertEquals("SELECT * FROM t_account where username='uuu'", sql);
    }

    @Test
    public void testSetArgs2() {
        String sql = "SELECT * FROM t_account where username=#username";
        Map<Object, Object> args = Convert.toMap("username", "uuu");
        sql = queryBuilder.setArgMap(sql, args);
        assertEquals("SELECT * FROM t_account where username='uuu'", sql);
    }

    @Test
    public void testSetPage() {
        String sql = "SELECT * FROM t_account";
        sql = queryBuilder.setPage(sql, new Page(1, 1));
        assertEquals("SELECT * FROM t_account LIMIT 0,1", sql);
    }

    @Test
    public void update() {
        String sql = "UPDATE t_account SET username='username-1',password='password-1',email='email-1' WHERE id=1";
        assertEquals(sql, queryBuilder.update(user));
    }

    @Test
    public void updateBySql() {
        String sql = queryBuilder.updateBySql("SET email=? WHERE id>?", new Object[] { "eml", 3 });
        assertEquals("UPDATE t_account SET email='eml' WHERE id>3", sql);
    }
}
