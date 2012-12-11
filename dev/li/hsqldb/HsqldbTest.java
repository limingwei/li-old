package li.hsqldb;

import java.sql.SQLException;

import javax.sql.DataSource;

import li.annotation.Inject;
import li.ioc.Ioc;
import li.test.BaseTest;

import org.junit.Test;

public class HsqldbTest extends BaseTest {
    @Inject
    Account dao;

    @Test
    public void test() {
        dao.list(null);
    }

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