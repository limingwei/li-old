package li.quartz;

public class Demo {
    /**
     * JUnit不支持多线程的测试,不能用于测试Quartz
     */
    public static void main(String[] args) {
        // Ioc.get(Quartz.class);// 在Xml中配置li.quartz.Quartz为一个Bean,可以使Ioc启动时启动Quartz

        new Quartz();
    }
}