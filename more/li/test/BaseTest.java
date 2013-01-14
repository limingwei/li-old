package li.test;

import li.dao.Page;

import org.junit.runner.RunWith;

/**
 * 测试类的基类，会为每一个@Inject注解的字段设值
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-07-21)
 */
@RunWith(JUnit4Li.class)
public class BaseTest {
    /**
     * 模拟的 li.dao.Page
     */
    protected Page page;

    /**
     * 初始化时为每一个@Inject注解的属性设值
     * 
     * @see li.test.JUnit4Li#createTest()
     */
    public BaseTest() {
        this.page = new Page();
        // li.test.JUnit4Li.createTest()
    }
}