package li.mock;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import li.util.Files;

/**
 * MockServletContext
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-09-27)
 */
public class MockServletContext implements ServletContext {
    private static final String WEB_ROOT_DIR_NAME = Files.config().getProperty("mock.webRoot", "WebContent");

    private Map<String, Object> servletContextMap;

    public MockServletContext() {
        this.servletContextMap = new HashMap<String, Object>();
    }

    public String getRealPath(String path) {
        return System.getProperty("user.dir") + "/" + WEB_ROOT_DIR_NAME + "/" + path;
    }

    public Object getAttribute(String key) {
        return servletContextMap.get(key);
    }

    public Enumeration<String> getAttributeNames() {
        return new Vector(servletContextMap.keySet()).elements();
    }

    public void removeAttribute(String key) {
        servletContextMap.remove(key);
    }

    public void setAttribute(String key, Object value) {
        servletContextMap.put(key, value);
    }

    public void addListener(Class<? extends EventListener> listener) {}

    public void addListener(String arg0) {}

    public <T extends EventListener> void addListener(T arg0) {}

    public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
        return null;
    }

    public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
        return null;
    }

    public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
        return null;
    }

    public void declareRoles(String... arg0) {}

    public ClassLoader getClassLoader() {
        return null;
    }

    public ServletContext getContext(String arg0) {
        return null;
    }

    public String getContextPath() {
        return null;
    }

    public int getEffectiveMajorVersion() {
        return 0;
    }

    public int getEffectiveMinorVersion() {
        return 0;
    }

    public String getInitParameter(String key) {
        return null;
    }

    public Enumeration<String> getInitParameterNames() {
        return null;
    }

    public int getMajorVersion() {
        return 0;
    }

    public String getMimeType(String arg0) {
        return null;
    }

    public int getMinorVersion() {
        return 0;
    }

    public RequestDispatcher getNamedDispatcher(String arg0) {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String arg0) {
        return null;
    }

    public URL getResource(String arg0) throws MalformedURLException {
        return null;
    }

    public InputStream getResourceAsStream(String arg0) {
        return null;
    }

    public Set<String> getResourcePaths(String arg0) {
        return null;
    }

    public String getServerInfo() {
        return null;
    }

    public Servlet getServlet(String arg0) throws ServletException {
        return null;
    }

    public String getServletContextName() {
        return null;
    }

    public Enumeration<String> getServletNames() {
        return null;
    }

    public Enumeration<Servlet> getServlets() {
        return null;
    }

    public void log(String arg0) {}

    public void log(Exception arg0, String arg1) {

    }

    public void log(String arg0, Throwable arg1) {}

    public boolean setInitParameter(String arg0, String arg1) {
        return false;
    }
}