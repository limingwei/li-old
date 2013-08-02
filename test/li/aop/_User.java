package li.aop;

import li.annotation.Aop;
import li.annotation.Bean;
import li.annotation.Trans;
import li.util.Log;

@Bean
public class _User {
    private static final Log log = Log.init();

    @Trans
    @Aop(_LogFilter.class)
    public String sayHi(String msg1, String msg2) {
        log.debug("user say hi");
        return msg1 + "\t" + msg2;
    }

    public void test() {
        try {
            Thread.sleep(123);
        } catch (Exception e) {}
        System.out.println("目标方法正在执行");
    }
}