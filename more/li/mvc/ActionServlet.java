package li.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import li.util.Log;

/**
 * MVC的Servlet ,负责分发HTTP请求
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.0 (2013-08-13)
 */
public class ActionServlet extends HttpServlet {
    private static final long serialVersionUID = -8214041532098707839L;

    Log log = Log.init();

    /**
     * 使用了ActionFilter的实现代码
     */
    private ActionFilter actionFilter;

    /**
     * 初始化
     */
    public void init(ServletConfig config) throws ServletException {
        this._init(config.getServletContext());
    }

    /**
     * 初始化Servlet
     */
    void _init(ServletContext servletContext) {
        actionFilter = new ActionFilter();
        actionFilter._init(servletContext);
        actionFilter.log = log;
    }

    /**
     * 服务
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!this._service(request, response)) {
            log.info("ACTION NOT FOUND: path=\"?\",method=\"?\"", ((HttpServletRequest) request).getServletPath(), ((HttpServletRequest) request).getMethod());
        }
    }

    /**
     * 受理请求
     */
    Boolean _service(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return this.actionFilter._service(request, response);
    }
}