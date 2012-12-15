package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Resource;

@Bean
public class ResourceAction extends AbstractAction implements Const {
    @Inject
    Resource resourceDao;

    @At("resource_list.do")
    public void list(Page page) {
        setRequest(LIST, resourceDao.list(page));
        setRequest(PAGE, page);
        view("resource/list");
    }
}