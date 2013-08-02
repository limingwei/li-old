package li.util;

import li.test.BaseTest;

import org.apache.log4j.Logger;
import org.junit.Test;

public class LogTest extends BaseTest {
    private static final Log log = Log.init();

    static final Integer TIMES = 100;

    @Test
    public void test() {
        log.info("LOG测试信息");
    }

    @Test
    public void testLog1() {// 3700 一代
        Log log = Log.init();
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            log.debug("111111111111111111111111111111111111111111111111111111?11111111111111111111111111111111111111111111111");
        }
        System.err.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testLog2() {// 5000 一代 参数
        Log log = Log.init();
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            log.debug("1111111111111111111111111111111????11111111111111111111111?11111111111111111111111111111111111111111111111", "hello", "hello", "hello", "hello", "hello");
        }
        System.err.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testLog3() {// 3400 二代
        Log2 log = Log2.init();
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            log.debug("1111111111111111111111111111111????11111111111111111111111?11111111111111111111111111111111111111111111111");
        }
        System.err.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testLog4() {// 4900 二代 参数
        Log2 log = Log2.init();
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            log.debug("1111111111111111111111111111111????11111111111111111111111?11111111111111111111111111111111111111111111111", "hello", "hello", "hello", "hello", "hello");
        }
        System.err.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testLog5() {// 3000 原生
        Logger logger = Logger.getLogger(getClass());
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            logger.debug("1111111111111111111111111111111????11111111111111111111111?11111111111111111111111111111111111111111111111");
        }
        System.err.println(System.currentTimeMillis() - start);
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