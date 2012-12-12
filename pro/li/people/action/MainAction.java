package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.mvc.AbstractAction;

@Bean
public class MainAction extends AbstractAction {

    @At("index.do")
    public void index() {
        view("index");
    }

    @At("top.do")
    public void top() {
        view("top");
    }

    @At("left.do")
    public void left() {
        view("left");
    }

    @At("right.do")
    public void right() {
        view("right");
    }

    @At("page.do")
    public void page() {
        view("page");
    }

    @At("foot.do")
    public void foot() {
        view("foot");
    }
}