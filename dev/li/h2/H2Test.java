package li.h2;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import li.ioc.Ioc;
import li.test.BaseTest;

import org.junit.Test;

public class H2Test extends BaseTest {
    Account dao = Ioc.get(Account.class);

    @Test
    public void test() {
        List<Account> list = dao.list(page.setPageSize(3));
        for (Account account : list) {
            System.out.println(account.get("id") + "\t" + account.get("USERNAME") + "\t" + account.get("PASSWORD") + "\t" + account.get("EMAIL"));
        }
    }

    @Test
    public void insert() {
        for (int i = 0; i < 3; i++) {
            Account account = new Account().set("STATUS", 1).set("USERNAME", "li" + System.currentTimeMillis()).set("PASSWORD", "wode").set("EMAIL", "limingwei@mail.com");
            System.out.println(dao.save(account) + "\t" + account.get("ID"));
        }
    }

    // @Test
    public void create_table() {
        String sql = "CREATE TABLE t_account" + //
                "(id int PRIMARY KEY AUTO_INCREMENT," + //
                "username varchar(255) UNIQUE NOT NULL ," + //
                "password varchar(255) NOT NULL," + //
                "email varchar(255) NOT NULL," + //
                "status int NOT NULL DEFAULT 1)";
        try {
            System.out.println(Ioc.get(DataSource.class, "h2").getConnection().prepareStatement(sql).executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}