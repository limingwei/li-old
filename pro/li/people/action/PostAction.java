package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.mvc.AbstractAction;
import li.people.record.Post;

@Bean
public class PostAction extends AbstractAction {
    @Inject
    Post postDao;

    @At("news.do")
    public void index() {
        view("news/index");
    }
}