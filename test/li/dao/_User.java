package li.dao;

import java.sql.ResultSet;

import li.annotation.Bean;
import li.annotation.Table;
import li.model.Field;

@Bean
@Table("t_account")
public class _User extends Record<_User> {
    private static final long serialVersionUID = -2274465783698819130L;

    @li.annotation.Trans
    public void testMultipleTrans2() {
        new Trans() {
            public void run() {
                testMultipleTrans3();
            }
        };
    }

    @li.annotation.Trans
    public void testMultipleTrans3() {
        new Trans() {
            public void run() {
                new Trans() {
                    public void run() {
                        updateIgnoreNull(new _User().set("id", 2).set("username", "u-5" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1").set("status", 1));
                    }
                };
            }
        };
    }

    public void testDesc() throws Exception {
        ResultSet resultSet = getConnection().prepareStatement("DESC t_account").executeQuery();
        ModelBuilder modelBuilder = new ModelBuilder(null, resultSet);
        System.out.println(modelBuilder.list(Record.class, Field.list(resultSet), 100, false));
    }
}