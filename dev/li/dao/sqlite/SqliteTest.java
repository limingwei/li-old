package li.dao.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import li.annotation.Inject;
import li.ioc.Ioc;
import li.test.BaseTest;
import li.util.Log;

import org.junit.Before;
import org.junit.Test;

public class SqliteTest extends BaseTest {
    private static final Log log = Log.init();
    @Inject
    Account accountDao;

    @Before
    public void before() {
        Account account = new Account();
        account.set("id", System.currentTimeMillis() / 10000).set("username", "uuu_" + System.currentTimeMillis()).set("password", "ppp").set("email", "eee").set("status", 1);
        accountDao.insert(account);
    }

    @Test
    public void test() {
        log.debug(accountDao.list(page.setPageSize(3), "WHERE 1=1 ORDER BY id DESC"));
        System.out.println(accountDao.getDataSource());
    }

    // @Test
    public void create_table() {
        String sql = "CREATE TABLE t_account" + //
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," + //
                "username varchar(255) UNIQUE NOT NULL ," + //
                "password varchar(255) NOT NULL," + //
                "email varchar(255) NOT NULL," + //
                "status int NOT NULL DEFAULT 1)";
        try {
            log.debug(Ioc.get(DataSource.class, "sqlite").getConnection().prepareStatement(sql).executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connection() throws Throwable {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:../../Program Files/sqlite/db/forum.db");
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM user where 1=2");

        ResultSetMetaData meta = resultSet.getMetaData();
        for (int columnCount = (null == meta ? -1 : meta.getColumnCount()), i = 1; i <= columnCount; i++) {
            log.debug(meta.getColumnName(i));
        }
    }
}
