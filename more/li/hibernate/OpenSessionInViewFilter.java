package li.hibernate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import li.ioc.Ioc;

import org.hibernate.Session;

/**
 * OpenSessionInViewFilter
 * 
 * @author 明伟
 */

public class OpenSessionInViewFilter implements Filter {
    static final ThreadLocal<Session> SESSION_THREADLOCAL = new ThreadLocal<Session>();

    private SessionFactory sessionFactory;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.sessionFactory = Ioc.get(SessionFactory.class);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        SESSION_THREADLOCAL.set(this.sessionFactory.openSession());
        filterChain.doFilter(servletRequest, servletResponse);
        SESSION_THREADLOCAL.get().clear();
        SESSION_THREADLOCAL.get().close();
    }

    public void destroy() {}
}