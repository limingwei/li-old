package demo.record;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.demo.record.Member;
import li.test.BaseTest;

import org.junit.Before;
import org.junit.Test;

public class MemberTest extends BaseTest {
    @Inject
    Member member;

    @Before
    public void before() {
        assertNotNull(member);
    }

    @Test
    public void testList() {}
}