package li.demo.record;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.people.record.Post;
import li.test.BaseTest;
import li.util.Log;

import org.junit.Before;
import org.junit.Test;

public class PostTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    Post post;

    @Before
    public void before() {
        assertNotNull(post);
    }

    @Test
    public void testList() {
        log.debug("li.demo.record.PostTest.testList()");
    }
}
