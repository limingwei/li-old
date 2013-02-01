package li.util;

import static org.junit.Assert.assertTrue;
import li.dao.Page;

import org.junit.Test;

public class PageTest {
    @Test
    public void test() {
        Page page = new Page();
        page.setRecordCount(100);
        assertTrue(page.getRecordCount().equals(100));
    }
}