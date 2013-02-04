package demo.record;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.demo.record.Post;
import li.test.BaseTest;

import org.junit.Before;
import org.junit.Test;

public class PostTest extends BaseTest {
    @Inject
    Post post;

    @Before
    public void before() {
        assertNotNull(post);
    }

    @Test
    public void testList() {}
}
