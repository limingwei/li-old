package demo.action;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.demo.action.MemberAction;
import li.test.ActionTest;

import org.junit.Before;
import org.junit.Test;

public class MemberActionTest extends ActionTest {
    @Inject
    MemberAction memberAction;

    @Before
    public void before() {
        assertNotNull(memberAction);
    }

    @Test
    public void find() {
        // memberAction.find(1);
    }

    @Test
    public void test1() {}

    @Test
    public void testFreemarker() {}

    @Test
    public void testVelocity() {}

}