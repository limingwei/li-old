package demo.action;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.people.action.PostAction;
import li.test.ActionTest;

import org.junit.Before;
import org.junit.Test;

public class PostActionTest extends ActionTest {
    @Inject
    PostAction postAction;

    @Before
    public void before() {
        assertNotNull(postAction);
    }

    @Test
    public void testList() {}
}