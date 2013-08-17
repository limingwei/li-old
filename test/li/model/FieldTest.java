package li.model;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import li.dao.QueryRunner;
import li.dao.test._User;
import li.ioc.Ioc;
import li.test.BaseTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldTest extends BaseTest {
    private Class<?> targetType;
    private Boolean annotated;
    private DataSource dataSource;
    private String table;

    private ResultSet resultSet;
    private Connection connection;
    private QueryRunner queryRunner;

    @Before
    public void before() {
        targetType = _User.class;
        annotated = true;
        dataSource = Ioc.get(DataSource.class);
        table = "t_account";

        try {
            connection = dataSource.getConnection();
            queryRunner = new QueryRunner(connection);
            resultSet = queryRunner.executeQuery("SELECT * FROM " + table + " WHERE 1=2");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void after() {
        try {
            resultSet.close();
            queryRunner.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listByAnnotation() {
        List<Field> fields = Field.list(targetType, annotated);
        assertNotNull(fields);
    }

    @Test
    public void listByTableName() {
        List<Field> fields = Field.list(dataSource, table);
        assertNotNull(fields);
    }

    @Test
    public void listByResultSet() {
        List<Field> fields = Field.list(resultSet);
        assertNotNull(fields);
    }
}