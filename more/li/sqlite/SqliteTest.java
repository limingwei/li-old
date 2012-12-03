package li.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import li.ioc.Ioc;
import li.test.BaseTest;

import org.junit.Test;

public class SqliteTest extends BaseTest {
    Account userDao = Ioc.get(Account.class);

    @Test
    public void test() {
        Account user = new Account().set("username", "li" + System.currentTimeMillis()).set("password", "wode").set("email", "limw@w.cn");
        userDao.save(user);

        System.out.println("user id = " + user.get("id"));

        for (Account u : userDao.list(page.setPageSize(3), "WHERE 1=1 ORDER BY id DESC")) {
            System.out.println(u.get("id") + "\t" + u.get("username") + "\t" + u.get("password") + "\t" + u.get("email"));
        }

        System.out.println("RecordCount = " + page.getRecordCount());
    }

    @Test
    public void insert() {
        for (int i = 0; i < 3; i++) {
            Account user = new Account().set("username", "li" + System.currentTimeMillis()).set("password", "wode").set("email", "limw@w.cn");
            System.out.println(userDao.save(user) + "\t" + user.get("id"));
        }
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
            System.out.println(Ioc.get(DataSource.class, "sqlite").getConnection().prepareStatement(sql).executeUpdate());
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
            System.out.println(meta.getColumnName(i));
        }
    }
}
