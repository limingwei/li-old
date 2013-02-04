package li.util;

import li.test.BaseTest;

import org.junit.Test;

public class LogTest extends BaseTest {
    private static final Log log = Log.init();

    @Test
    public void test() {
        log.info("LOG测试信息");
    }

    @Test
    public void test2() {
        log.info("1 ? ? ? ?", 1);
        log.info("2 ?", 1, 2, 3, 4);
        log.info("3 ?");
        Object arg = null;
        log.info("4 ?", arg);
    }

    @Test
    public void put() {
        Log.put("123", "123");
    }

    @Test
    public void get() {
        log.debug(Log.get("123"));
    }
}