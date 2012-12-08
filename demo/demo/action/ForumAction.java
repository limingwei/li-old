package demo.action;

import li.annotation.Arg;
import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import demo.record.Forum;
import demo.record.Thread;

@Bean
public class ForumAction extends AbstractAction {
    @Inject
    Forum forumDao;

    @Inject
    Thread threadDao;

    @At({ "forum_list" })
    public void list(@Arg("pn") Page page) {
        setRequest("forums", forumDao.list(page));
        view("forum_list");
    }

    @At({ "forum" })
    public void get(Integer id, @Arg("pn") Page page) {
        setRequest("forums", forumDao.list(page.setPageNumber(1)));
        setRequest("threads", threadDao.listByForumId(id, page));
        setRequest("forum", forumDao.find(id));
        setSession("page", page);
        view("forum_show");
    }
}