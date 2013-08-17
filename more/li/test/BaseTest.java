package li.test;

import li.dao.Page;

import org.junit.runner.RunWith;

/**
 * 可以作为你的测试类的基类
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-07-21)
 * @see li.test.JUnit4Li#createTest()
 */
@RunWith(JUnit4Li.class)
public class BaseTest {

    protected Page page = new Page();

    protected Page MAX_PAGE = new Page(1, Integer.MAX_VALUE);
}