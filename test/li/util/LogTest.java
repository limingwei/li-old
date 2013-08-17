package li.util;

import li.test.BaseTest;

import org.apache.log4j.Logger;
import org.junit.Test;

public class LogTest extends BaseTest {
    private static final Log log = Log.init();

    static final Integer TIMES = 3;

    @Test
    public void testLog1() {// 3700 一代
        Log log = Log.init();
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            log.debug("111111111?111111111111");
        }
        log.debug("一代 执行log ? 次 耗时 ? 毫秒", TIMES, System.currentTimeMillis() - start);
    }

    @Test
    public void testLog2() {// 5000 一代 参数
        Log log = Log.init();
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            log.debug("11111????11111111?111111", "hello", "hello", "hello", "hello", "hello");
        }
        log.debug("一代 参数 执行log ? 次 耗时 ? 毫秒", TIMES, System.currentTimeMillis() - start);
    }

    @Test
    public void testLog3() {// 3400 二代
        Log2 log = Log2.init();
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            log.debug("1111111????111111?1111111");
        }
        log.debug("二代 执行log ? 次 耗时 ? 毫秒", TIMES, System.currentTimeMillis() - start);
    }

    @Test
    public void testLog4() {// 4900 二代 参数
        Log2 log = Log2.init();
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            log.debug("11111111????1111111?11111111", "hello", "hello", "hello", "hello", "hello");
        }
        log.debug("二代 参数 执行log ? 次 耗时 ? 毫秒", TIMES, System.currentTimeMillis() - start);
    }

    @Test
    public void testLog5() {// 3000 原生
        Logger logger = Logger.getLogger(getClass());
        Long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            logger.debug("111111????111111?11111");
        }
        log.debug("原生 执行log ? 次 耗时 ? 毫秒", TIMES, System.currentTimeMillis() - start);
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