package demo.action;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.demo.action.ForumAction;
import li.test.ActionTest;

import org.junit.Before;
import org.junit.Test;

public class ForumActionTest extends ActionTest {
    @Inject
    ForumAction forumAction;

    @Before
    public void before() {
        assertNotNull(forumAction);
    }

    @Test
    public void list() {
        // forumAction.list(page);
    }

    @Test
    public void get() {
        // forumAction.get(1, page);
    }
}