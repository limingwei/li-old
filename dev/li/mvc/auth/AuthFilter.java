package li.mvc.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import li.aop.AopChain;
import li.aop.AopFilter;
import li.mvc.Context;

/**
 * @author : 明伟 
 * @date : 2013年7月26日 下午2:53:11
 * @version 1.0 
 */
public abstract class AuthFilter implements Filter, AopFilter {
    public void init(FilterConfig config) throws ServletException {}

    public void destroy() {}

    /**
     * Servlet
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (needAuth(request) && !accessAuth(request)) {// 需要验证 并且 未通过验证
            failAuth();
        } else {// 不需要验证 或者 已通过验证
            chain.doFilter(request, servletResponse);
        }
    }

    /**
     * Aop
     */
    public void doFilter(AopChain chain) {
        HttpServletRequest request = Context.getRequest();
        if (needAuth(request) && !accessAuth(request)) {// 需要验证 并且 未通过验证
            failAuth();
        } else {// 不需要验证 或者 已通过验证
            chain.doFilter();
        }
    }

    /**
     * 当前请求是否需要验证
     */
    public abstract Boolean needAuth(HttpServletRequest request);

    /**
     * 当前请求是否通过验证
     */
    public abstract Boolean accessAuth(HttpServletRequest request);

    /**
     * 无权限访问当前资源时候的返回
     */
    public abstract void failAuth();
}