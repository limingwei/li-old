package demo.action;

import li.annotation.Arg;
import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Trans;
import li.dao.Page;
import li.mvc.AbstractAction;
import demo.record.Post;
import demo.record.Thread;

@Bean
public class ThreadAction extends AbstractAction {
    @Inject
    Thread threadDao;

    @Inject
    Post postDao;

    @At("thread_list")
    public void list(Integer id, @Arg("pn") Page page) {
        setRequest("threads", threadDao.listByForumId(id, page));
        view("WEB-INF/view/thread_list.jsp");
    }

    @At({ "thread" })
    public void get(Integer id, @Arg("pn") Page page) {
        setRequest("thread", threadDao.find(id));
        setRequest("posts", postDao.listByThreadId(id, page));
        setSession("page", page);
        view("thread_show");
    }

    @At(value = "thread_save", method = POST)
    public void save(Thread thread) {
        threadDao.save(thread);
        redirect(getRequest().getHeader("referer"));
    }

    @At("thread_edit")
    public void edit(Integer id) {
        setRequest("thread", threadDao.find(id));
        view("WEB-INF/view/thread_edit.jsp");
    }

    @At(value = "thread_update", method = POST)
    public void update(Thread thread) {
        threadDao.update(thread);
        redirect("thread?id=" + thread.get("id"));
    }

    @At("thread_delete")
    @Trans
    public void delete(Integer id, @Arg("forum_id") Integer forumId, Integer pn) {
        threadDao.delete(id);
        postDao.deleteByThreadId(id);
        redirect("forum?id=" + forumId + "&pn=" + pn);
    }
}