package li.demo.action;

import li.annotation.Inject;
import li.people.action.DemoAction;
import li.test.ActionTest;

import org.junit.Test;

public class DemoActionTest extends ActionTest {
    @Inject
    private DemoAction demoAction;

    @Test
    public void beetl() {
        demoAction.testBeetl();
    }

    @Test
    public void freemarker() {
        demoAction.testFreemarker();
    }

    @Test
    public void velocity() {
        demoAction.testVelocity();
    }

    @Test
    public void forward() {
        demoAction.testJSP();
    }

    @Test
    public void redirect() {
        demoAction.redirect("g.cn");
    }

    @Test
    public void write() {
        demoAction.write("测试写内容到页面");
    }

    @Test
    public void view() {
        demoAction.view("wt:测试view方法");
    }

    @Test
    public void test404() {
        demoAction.test404();
    }
}