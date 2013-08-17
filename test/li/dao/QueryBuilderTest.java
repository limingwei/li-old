package li.dao;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import li.ioc.Ioc;
import li.model.Bean;
import li.people.record.Account;
import li.test.BaseTest;
import li.util.Convert;

import org.junit.Before;
import org.junit.Test;

public class QueryBuilderTest extends BaseTest {
    DataSource dataSource = Ioc.get(DataSource.class);

    QueryBuilder queryBuilder = new QueryBuilder();

    Account account = new Account();

    @Before
    public void before() throws Exception {
        queryBuilder.beanMeta = Bean.getMeta(dataSource, Account.class);
        account.set("id", 1);
        account.set("username", "username-1");
        account.set("password", "password-1");
        account.set("email", "email-1");
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
    public void deleteById() {
        assertEquals("DELETE FROM t_account WHERE id=1", queryBuilder.deleteById(1));
    }

    @Test
    public void deleteBySql() {
        assertEquals("DELETE FROM t_account WHERE id='1'", queryBuilder.deleteBySql("WHERE id=?", new Object[] { "1" }));
    }

    @Test
    public void findById() {
        assertEquals("SELECT * FROM t_account WHERE id=123", queryBuilder.findById(123));
    }

    @Test
    public void findBySql() {
        assertEquals("SELECT * FROM t_account WHERE id=1", queryBuilder.findBySql("WHERE id=?", new Object[] { 1 }));
    }

    @Test
    public void insert() {
        assertEquals("INSERT INTO t_account (id,username,password,email,role_id,flag) VALUES (NULL,'username-1','password-1','email-1',NULL,NULL)", queryBuilder.insert(account.set("id", null)));
    }

    @Test
    public void insertIgnoreNull() {
        assertEquals("INSERT INTO t_account (username,password,email) VALUES ('username-1','password-1','email-1')", queryBuilder.insertIgnoreNull(account.set("id", null)));

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
    public void setArgMap() {
        assertEquals("SELECT * FROM WHERE id=1 OR username LIKE '%li%'", queryBuilder.setArgMap("SELECT * FROM WHERE id=#id OR username LIKE #username", Convert.toMap("id", 1, "username", "%li%")));
    }

    @Test
    public void setArgs() {
        assertEquals("SELECT * FROM WHERE id=1 OR username LIKE '%li%'", queryBuilder.setArgs("SELECT * FROM WHERE id=? OR username LIKE ?", new Object[] { 1, "%li%" }));

        assertEquals("SELECT * FROM t_account where username='uuu'", queryBuilder.setArgs("SELECT * FROM t_account where username=?", new Object[] { "uuu" }));

        assertEquals("SELECT * FROM t_account where username='uuu'", queryBuilder.setArgMap("SELECT * FROM t_account where username=#username", Convert.toMap("username", "uuu")));
    }

    @Test
    public void setPage() {
        assertEquals("SELECT * FROM t_account LIMIT 0,10", queryBuilder.setPage("SELECT * FROM t_account", page));

        assertEquals("SELECT * FROM t_account LIMIT 0,1", queryBuilder.setPage("SELECT * FROM t_account", new Page(1, 1)));
    }

    @Test
    public void update() {
        assertEquals("UPDATE t_account SET username='username-1',password='password-1',email='email-1',role_id=NULL,flag=NULL WHERE id=1", queryBuilder.update(account));
    }

    @Test
    public void updateBySql() {
        assertEquals("UPDATE t_account SET email='eml' WHERE id>3", queryBuilder.updateBySql("SET email=? WHERE id>?", new Object[] { "eml", 3 }));
    }

    @Test
    public void updateIgnoreNull() {
        assertEquals("UPDATE t_account SET username='username-1',password='password-1',email='email-1' WHERE id=1", queryBuilder.updateIgnoreNull(account));
    }

    @Test
    public void wrap() {
        assertEquals("123", queryBuilder.wrap(123));
        assertEquals("'abc'", queryBuilder.wrap("abc"));
    }
}
