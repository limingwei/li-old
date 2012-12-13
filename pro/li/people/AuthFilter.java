package li.people;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getServletPath();
        List<String> permissions = (List<String>) ((HttpServletRequest) request).getSession().getAttribute("permissions");
        if (permissions.contains(path)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletRequest) request).getRequestDispatcher("deny").forward(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {}

    public void destroy() {}
}