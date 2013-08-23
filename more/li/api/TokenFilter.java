package li.api;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * TokenFilter
 * 
 * @author 明伟
 */
public class TokenFilter implements Filter {
    private static final Map<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<String, HttpSession>();

    private String tokenName = "_token";

    public void destroy() {}

    public void init(FilterConfig config) throws ServletException {
        String _name = config.getInitParameter("tokenName");
        if (null != _name) {
            this.tokenName = _name;
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        chain.doFilter(new HttpServletRequestWrapper(request) {
            private String token;

            public HttpSession getSession() {
                token = super.getParameter(tokenName);
                HttpSession httpSession = null;

                if (null != token) {
                    httpSession = SESSION_MAP.get(token);
                    if (null == httpSession) {
                        httpSession = super.getSession();
                        SESSION_MAP.put(token, httpSession);
                    }
                } else {// null == token
                    token = newToken();
                    httpSession = super.getSession();
                    SESSION_MAP.put(token, httpSession);
                }

                return httpSession;
            }

            /**
             * 一定要在getSession之后
             */
            public String getRequestedSessionId() {
                return token;
            }
        }, response);
    }

    /**
     * newToken
     */
    public String newToken() {
        return UUID.randomUUID().toString();
    }
}