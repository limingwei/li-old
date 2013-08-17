package li.dao.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import li.annotation.Inject;
import li.ioc.Ioc;
import li.test.BaseTest;

import org.junit.Before;
import org.junit.Test;

public class DerbyTest extends BaseTest {
    @Inject
    Account accountDao;

    @Before
    public void before() {
        Account account = new Account();
        account.set("ID", System.currentTimeMillis() / 10000).set("USERNAME", "uuu_" + System.currentTimeMillis()).set("PASSWORD", "ppp").set("EMAIL", "eee").set("STATUS", 1);
        accountDao.insert(account);
    }

    @Test
    public void test() {
        System.out.println(accountDao.list(null));
        System.out.println(accountDao.getDataSource());
    }

    public void create_table() {
        String sql = "CREATE TABLE t_account" + //
                "(id INTEGER PRIMARY KEY ," + //
                "username varchar(255) UNIQUE NOT NULL ," + //
                "password varchar(255) NOT NULL," + //
                "email varchar(255) NOT NULL," + //
                "status int NOT NULL DEFAULT 1)";
        try {
            System.out.println(Ioc.get(DataSource.class, "derby").getConnection().prepareStatement(sql).executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void main() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            System.out.println("Load the embedded driver");
            Connection conn = null;
            Properties props = new Properties();
            props.put("user", "user1");
            props.put("password", "user1");
            conn = DriverManager.getConnection("jdbc:derby:f:\\myderby\\forum;create=true", props);
            System.out.println("create and connect to helloDB");
            conn.setAutoCommit(false);

            Statement s = conn.createStatement();
            s.execute("create table hellotable(name varchar(40), score int)");
            System.out.println("Created table hellotable");
            s.execute("insert into hellotable values('Ruth Cao', 86)");
            s.execute("insert into hellotable values ('Flora Shi', 92)");

            ResultSet rs = s.executeQuery("SELECT name, score FROM hellotable ORDER BY score");
            System.out.println("name\t\tscore");
            while (rs.next()) {
                StringBuilder builder = new StringBuilder(rs.getString(1));
                builder.append("\t");
                builder.append(rs.getInt(2));
                System.out.println(builder.toString());
            }
            System.out.println("Dropped table hellotable");

            rs.close();
            s.close();
            System.out.println("Closed result set and statement");
            conn.commit();
            conn.close();
            System.out.println("Committed transaction and closed connection");

            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                System.out.println("Database shut down normally");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
