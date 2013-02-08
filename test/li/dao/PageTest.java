package li.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PageTest {
    @Test
    public void test() {
        Page page = new Page();
        page.setRecordCount(100);
        assertTrue(page.getRecordCount().equals(100));
    }
}