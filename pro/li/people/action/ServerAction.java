package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Server;

@Bean
public class ServerAction extends AbstractAction implements Const{
    @Inject
    Server serverDao;
    
    @At("server_list.do")
    public void list(Page page) {
        setRequest(LIST, serverDao.list(page));
        setRequest(PAGE, page);
        view("server/list");
    }
}