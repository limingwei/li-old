package li.demo.action;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.people.action.PostAction;
import li.test.ActionTest;
import li.util.Log;

import org.junit.Before;
import org.junit.Test;

public class PostActionTest extends ActionTest {
    private static final Log log = Log.init();

    @Inject
    PostAction postAction;

    @Before
    public void before() {
        assertNotNull(postAction);
    }

    @Test
    public void testList() {
        log.debug("li.demo.action.PostActionTest.testList()");
    }
}