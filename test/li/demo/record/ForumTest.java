package li.demo.record;

import li.annotation.Inject;
import li.people.record.Forum;
import li.test.BaseTest;

import org.junit.Test;

public class ForumTest extends BaseTest {
    @Inject
    Forum forumDao;

    @Test
    public void list() {
        System.out.println(forumDao.list(page));
    }

    @Test
    public void saveIgnoreNull() {
        for (int i = 0; i < 5; i++) {
            Forum forum = new Forum().set("name", "forum_name_" + i + "_" + System.currentTimeMillis());
            System.out.println(forumDao.saveIgnoreNull(forum));
        }
    }

}
