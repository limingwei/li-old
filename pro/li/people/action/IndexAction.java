package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.mvc.AbstractAction;

@Bean
public class IndexAction extends AbstractAction {

    @At("index.do")
    public void index() {
        if (null == getSession("account")) {
            redirect("login.do?from_index");
        } else {
            view("index");
        }
    }

    @At("bootstrap_datepicker.do")
    public void bootstrap_datepicker() {
        view("bootstrap_datepicker");
    }

    @At("bootstrap_validation.do")
    public void bootstrap_validation() {
        view("bootstrap_validation");
    }
}