package demo.action;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.demo.action.ThreadAction;
import li.test.ActionTest;

import org.junit.Before;
import org.junit.Test;

public class ThreadActionTest extends ActionTest {
    @Inject
    ThreadAction threadAction;

    @Before
    public void before() {
        assertNotNull(threadAction);
    }

    @Test
    public void testList() {
        // threadAction.list(1, page);
    }
}
