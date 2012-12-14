package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.mvc.AbstractAction;

@Bean
public class IndexAction extends AbstractAction {

    @At("index.do")
    public void index() {
        view("index");
    }
}