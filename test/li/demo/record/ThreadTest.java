package li.demo.record;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import li.annotation.Inject;
import li.test.BaseTest;
import li.util.Convert;

import org.junit.Before;
import org.junit.Test;

public class ThreadTest extends BaseTest {
    @Inject
    Thread thread;

    @Before
    public void before() {
        assertNotNull(thread);
    }

    @Test
    public void listByForumId() {
        thread.listByForumId(1, page);
    }

    @Test
    public void testArgs() {
        String sql = "SELECT * FROM t_thread WHERE id=? OR id=#id1 OR id=? OR id=#id2";
        Map<?, ?> argMap = Convert.toMap("id1", 11, "id2", 12);
        thread.list(page, sql, 1, 2, 3, argMap);
    }

    @Test
    public void testList() {}
}