package li.dao.hsqldb;

import java.sql.SQLException;

import javax.sql.DataSource;

import li.annotation.Inject;
import li.ioc.Ioc;
import li.test.BaseTest;

import org.junit.Before;
import org.junit.Test;

public class HsqldbTest extends BaseTest {
    @Inject
    Account accountDao;

    @Before
    public void before() {
        Account account = new Account();
        account.set("ID", System.currentTimeMillis() / 10000).set("USERNAME", "uuu_" + System.currentTimeMillis()).set("PASSWORD", "ppp").set("EMAIL", "eee").set("STATUS", 1);
        accountDao.insert(account);
    }

    @Test
    public void list() {
        System.out.println(accountDao.list(null));
        System.out.println(accountDao.getDataSource());
    }

    // @Test
    public void create_table() {
        String sql = "CREATE TABLE t_account" + //
                "(id INT PRIMARY KEY," + //
                "username varchar(255) UNIQUE NOT NULL ," + //
                "password varchar(255) NOT NULL," + //
                "email varchar(255) NOT NULL," + //
                "status int NOT NULL)";
        try {
            System.out.println(Ioc.get(DataSource.class, "hsqldb").getConnection().prepareStatement(sql).executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}