package li.dao;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import javax.sql.DataSource;

import li.ioc.Ioc;
import li.model.Bean;
import li.test.BaseTest;
import li.util.Convert;

import org.junit.Before;
import org.junit.Test;

import demo.model.User;

public class QueryBuilderTest extends BaseTest {
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

    // @Test
    // public void setAlias() {
    // String expected = "SELECT t_account.id,t_account.username,t_account.password,t_account.email,t_account.role_id,t_account.status FROM t_account";
    // String actual = queryBuilder.setAlias("SELECT t_account.# FROM t_account");
    // assertEquals(expected, actual);
    // }

    @Test
    public void setArgMap() {
        String sql = "SELECT * FROM WHERE id=#id OR username LIKE #username";
        Map<Object, Object> map = Convert.toMap("id", 1, "username", "%li%");
        assertEquals("SELECT * FROM WHERE id='1' OR username LIKE '%li%'", queryBuilder.setArgMap(sql, map));
    }

    @Test
    public void setArgs() {
        String sql = "SELECT * FROM WHERE id=? OR username LIKE ?";
        Object[] args = { 1, "%li%" };
        assertEquals("SELECT * FROM WHERE id='1' OR username LIKE '%li%'", queryBuilder.setArgs(sql, args));
    }

    @Test
    public void setPage() {
        assertEquals("SELECT * FROM t_account LIMIT 0,10", queryBuilder.setPage("SELECT * FROM t_account", page));
    }

    // @Test
    // public void testSetAlias() {
    // String sql = "SELECT t_account.*,t_forum.# as f_#,t_member.#,t_post.# AS p_# FROM t_account";
    // String expected = "SELECT t_account.id,t_account.username,t_account.password,t_account.email,t_account.status," + //
    // "t_forum.id as f_id,t_forum.name as f_name,t_forum.status as f_status," + //
    // "t_member.id,t_member.name,t_member.account_id,t_member.status," + //
    // "t_post.id AS p_id,t_post.subject AS p_subject,t_post.content AS p_content,t_post.member_id AS p_member_id,t_post.thread_id AS p_thread_id,t_post.status AS p_status" + //
    // " FROM t_account";
    // assertEquals(expected, queryBuilder.setAlias(sql));
    // }

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
        assertEquals("UPDATE t_account SET email='eml' WHERE id>'3'", sql);
    }
}
