package li.service;

import static org.junit.Assert.assertNotNull;
import li.test.BaseTest;
import li.util.Log;

import org.junit.Test;

public class AbstractServiceTest extends BaseTest {
    private static final Log log = Log.init();

    @Test
    public void test() {
        log.debug("li.service.AbstractServiceTest.test()");
        assertNotNull(page);
    }
}