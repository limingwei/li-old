package li.aop.test;

import li.annotation.Aop;
import li.annotation.Bean;
import li.annotation.Trans;
import li.util.Log;
import li.util.ThreadUtil;

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
        ThreadUtil.sleep(123);
        log.debug("目标方法正在执行");
    }
}