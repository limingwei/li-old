package demo.action;

import li.annotation.Arg;
import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import demo.record.Post;

@Bean
public class PostAction extends AbstractAction {
    @Inject
    Post postDao;

    @At("post_list")
    public void list(@Arg("pn") Page page) {
        setRequest("posts", postDao.list(page));
        view("WEB-INF/view/post_list.jsp");
    }

    @At("post_get")
    public void get(Integer id) {
        setRequest("post", postDao.find(id));
        view("WEB-INF/view/post_get.jsp");
    }

    @At(value = "post.save", method = POST)
    public void save(Post post) {
        postDao.save(post);
        redirect(getRequest().getHeader("referer"));
    }

    @At("post_edit")
    public void edit(Integer id) {
        setRequest("post", postDao.find(id));
        view("WEB-INF/view/post_edit.jsp");
    }

    @At(value = "post_update", method = POST)
    public void update(Post post) {
        postDao.updateIgnoreNull(post);
        redirect("thread?id=" + post.get("thread_id"));
    }

    @At("post_delete")
    public void delete(Integer id, @Arg("thread_id") Integer threadId, Integer pn) {
        postDao.delete(id);
        redirect("thread?id=" + threadId + "&pn=" + pn);
    }
}