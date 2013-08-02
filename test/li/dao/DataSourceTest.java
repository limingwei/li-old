package li.dao;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import li.ioc.Ioc;
import li.test.BaseTest;

public class DataSourceTest extends BaseTest {

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final DataSource dataSource = Ioc.get("li");

        for (int i = 0; i < 30; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        Connection connection = dataSource.getConnection();
                        Thread.sleep(1000);
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.err.println("添加任务完成");
    }
}
