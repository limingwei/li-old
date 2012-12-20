package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.dao.Page;
import li.mvc.AbstractAction;

@Bean
public class ServerAction extends AbstractAction {
    @At("server_list.do")
    public void list(Page page) {
        view("server/list");
    }
}