package li.test;

import java.io.Writer;

import javax.servlet.ServletException;

import li.mock.MockFilterConfig;
import li.mock.MockHttpServletRequest;
import li.mock.MockHttpServletResponse;
import li.mock.MockHttpSession;
import li.mock.MockServletContext;
import li.model.Action;
import li.mvc.ActionFilter;
import li.mvc.Context;

/**
 * Action测试类的基类，准备了request response等的Mock对象
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-07-21)
 */
public class ActionTest extends BaseTest {
    /**
     * 模拟的response
     */
    protected MockHttpServletResponse response;

    /**
     * 模拟的request
     */
    protected MockHttpServletRequest request;

    /**
     * 模拟的session
     */
    protected MockHttpSession session;

    /**
     * 模拟Action
     */
    protected Action action;

    /**
     * 模拟的servletContext
     */
    protected static MockServletContext servletContext;

    /**
     * 模拟的FilterConfig
     */
    private static MockFilterConfig filterConfig;

    static {
        servletContext = new MockServletContext();
        filterConfig = new MockFilterConfig(servletContext);
        try {
            new ActionFilter().init(filterConfig);// 初始化Filter,设置一些环境变量,只执行一次
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化方法，由于不会启动Filter，所以这里为Context ThreadLocal设值
     */
    public ActionTest() {
        this.request = new MockHttpServletRequest();
        this.request.setServletContext(servletContext);
        this.response = new MockHttpServletResponse();
        this.session = request.getSession();
        this.action = new Action();

        Context.init(request, response, action);
    }

    public void setMethod(String method) {
        request.setMethod(method);
    }

    public void setWritter(Writer writer) {
        this.response.setWritter(writer);
    }
}