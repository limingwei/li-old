package li.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import li.util.Log;

/**
 * MVC分发器 ,负责分发HTTP请求
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.0 (2013-08-13)
 */
public class ActionDispatcher extends ActionServlet implements Filter {
    private static final long serialVersionUID = -4195486986869714390L;

    Log log = Log.init();

    /**
     * 受理请求
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!actionFilter._service((HttpServletRequest) request, (HttpServletResponse) response)) {
            chain.doFilter(request, response);
        }
    }

    /**
     * 初始化Servlet
     */
    public void init(ServletConfig config) throws ServletException {
        log = Log.init("li.mvc.ActionDispatcher as Servlet");
        super.log = log;
        super._init(config.getServletContext());
    }

    /**
     * 初始化Filter
     */
    public void init(FilterConfig config) throws ServletException {
        log = Log.init("li.mvc.ActionDispatcher as Filter");
        super.log = log;
        super._init(config.getServletContext());
    }
}