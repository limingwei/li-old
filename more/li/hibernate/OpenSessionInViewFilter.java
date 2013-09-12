package li.hibernate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import li.aop.AopChain;
import li.aop.AopFilter;
import li.ioc.Ioc;

import org.hibernate.Session;

/**
 * OpenSessionInViewFilter
 * 
 * @author 明伟
 */

public class OpenSessionInViewFilter implements Filter, AopFilter {
    static final ThreadLocal<Session> SESSION_THREADLOCAL = new ThreadLocal<Session>();

    private SessionFactory sessionFactory;

    public OpenSessionInViewFilter() {
        this.sessionFactory = Ioc.get(SessionFactory.class);
    }

    /**
     * for servlet filter
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        SESSION_THREADLOCAL.set(this.sessionFactory.openSession());
        filterChain.doFilter(servletRequest, servletResponse);
        this.closeSession(SESSION_THREADLOCAL.get());
    }

    /**
     * for AopFilter
     */
    public void doFilter(AopChain chain) {
        SESSION_THREADLOCAL.set(this.sessionFactory.openSession());
        chain.doFilter();
        closeSession(SESSION_THREADLOCAL.get());
    }

    static void closeSession(Session session) {
        if (null != session) {
            session.clear();
            session.close();
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {}

    public void destroy() {}
}