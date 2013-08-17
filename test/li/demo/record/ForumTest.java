package li.demo.record;

import li.annotation.Inject;
import li.people.record.Forum;
import li.test.BaseTest;
import li.util.Log;

import org.junit.Test;

public class ForumTest extends BaseTest {
    private static final Log log = Log.init();

    @Inject
    Forum forumDao;

    @Test
    public void list() {
        log.debug(forumDao.list(page));
    }

    @Test
    public void saveIgnoreNull() {
        for (int i = 0; i < 5; i++) {
            Forum forum = new Forum().set("name", "forum_name_" + i + "_" + System.currentTimeMillis());
            log.debug(forumDao.saveIgnoreNull(forum));
        }
    }
}