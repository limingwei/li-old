package li.task;

public class Cron4jTest {
    /**
     * JUnit不支持多线程的测试,不能用于测试Cron4j
     */
    public static void main(String[] args) {
        new Cron4j();
    }
}