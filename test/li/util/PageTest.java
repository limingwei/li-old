package li.util;

import static org.junit.Assert.assertEquals;
import li.dao.Page;

import org.junit.Test;

public class PageTest {
    @Test
    public void test() {
        Page page = new Page();
        page.setRecordCount(100);
        assertEquals(10 + "", page.getPageSize() + "");
    }
}