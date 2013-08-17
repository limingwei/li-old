package li.mock;

import java.util.Enumeration;

import javax.servlet.FilterConfig;

/**
 * MockFilterConfig
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-09-27)
 */
public class MockFilterConfig implements FilterConfig {

    private MockServletContext servletContext;

    public MockFilterConfig(MockServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public MockServletContext getServletContext() {
        return this.servletContext;
    }

    public Enumeration<String> getInitParameterNames() {
        return null;
    }

    public String getFilterName() {
        return null;
    }

    public String getInitParameter(String key) {
        return null;
    }
}