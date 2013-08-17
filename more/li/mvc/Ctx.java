package li.mvc;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import li.mvc.view.BeetlView;
import li.mvc.view.HttlView;
import li.mvc.view.VelocityView;
import li.util.Convert;
import li.util.Log;
import li.util.Reflect;

/**
 * Action工具类
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2013-02-15)
 * @see li.mvc.Context
 */
public class Ctx extends Context {
    private static final AbstractAction ABSTRACT_ACTION = new AbstractAction() {};
    private static Log log = Log.init();

    /**
     * 主视图方法,以冒号分割前缀表示视图类型
     * 
     * @see #velocity(String)
     * @see #beetl(String)
     * @see #httl(String)
     * @see li.mvc.Context#forward(String)
     * @see li.mvc.Context#freemarker(String)
     * @see li.mvc.Context#redirect(String)
     * @see li.mvc.Context#write(Object)
     * @see li.mvc.Context#view(String)
     */
    public static String view(String path) {
        String viewType = path.contains(":") ? path.split(":")[0] : VIEW_TYPE;// 视图类型
        String viewPath = path.startsWith(viewType + ":") ? path.split(viewType + ":")[1] : path;// path冒号后的部分或者path
        if ("velocity".equals(viewType) || "vl".equals(viewType)) {// velocity视图
            return velocity(VIEW_PREFIX + viewPath + VIEW_SUFFIX);
        } else if ("beetl".equals(viewType) || "bt".equals(viewType)) {// beetl视图
            return beetl(VIEW_PREFIX + viewPath + VIEW_SUFFIX);
        } else if ("httl".equals(viewType) || "ht".equals(viewType)) {// beetl视图
            return httl(VIEW_PREFIX + viewPath + VIEW_SUFFIX);
        } else {
            return Context.view(path);// 其他视图使用Context.view
        }
    }

    /**
     * 返回beetl视图
     */
    public static String beetl(String path) {
        try {
            new BeetlView().render(path, getResponse(), getAttributes());
        } catch (Throwable e) {
            error(e);
        }
        return "~!@#DONE";
    }

    /**
     * 返回velocity视图
     */
    public static String velocity(String path) {
        try {
            new VelocityView().render(path, getResponse(), getAttributes());
        } catch (Throwable e) {
            error(e);
        }
        return "~!@#DONE";
    }

    /**
     * 返回HTTL视图
     */
    public static String httl(String path) {
        try {
            new HttlView().render(path, getResponse(), getAttributes());
        } catch (Exception e) {
            error(e);
        }
        return "~!@#DONE";
    }

    /**
     * 上传文件
     */
    public static void upload(String uploadPath) {
        try {
            Object factory = Reflect.born("org.apache.commons.fileupload.disk.DiskFileItemFactory");
            Object upload = Reflect.born("org.apache.commons.fileupload.servlet.ServletFileUpload", new Class[] { Reflect.getType("org.apache.commons.fileupload.FileItemFactory") }, factory);
            List fileItems = (List) Reflect.invoke(upload, "parseRequest", getRequest());
            for (Object fileItem : fileItems) {
                File saveFile = new File(uploadPath, Reflect.invoke(fileItem, "getName").toString());
                Reflect.invoke(fileItem, "write", saveFile);
            }
            log.info("upload success");
        } catch (Throwable e) {
            error(e);
        }
    }

    /**
     * getServletPath
     */
    public static String getServletPath() {
        return getRequest().getServletPath();
    }

    /**
     * getRequestURI
     */
    public static String getRequestURI() {
        return getRequest().getRequestURI();
    }

    /**
     * getParameterMap
     */
    public static Map<String, String[]> getParameterMap() {
        return getRequest().getParameterMap();
    }

    /**
     * getParameter
     */
    public static Object getParameter(String key) {
        return getRequest().getParameter(key);
    }

    /**
     * getParameter
     */
    public static <C> C getParameter(String key, Class<C> type) {
        return Convert.toType(type, getParameter(key));
    }

    /**
     * getParameter
     */
    public static <C> C getParameter(String key, C defaultValue) {
        String result = getRequest().getParameter(key);
        return null == result || null == defaultValue ? defaultValue : (C) Convert.toType(defaultValue.getClass(), getParameter(key));
    }

    /**
     * setRequest
     */
    public static AbstractAction setRequest(String name, Object value) {
        getRequest().setAttribute(name, value);
        return ABSTRACT_ACTION;
    }

    /**
     * removeRequest
     */
    public static AbstractAction removeRequest(String name) {
        getRequest().removeAttribute(name);
        return ABSTRACT_ACTION;
    }

    /**
     * setRequest
     */
    public static AbstractAction setRequest(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            setRequest(entry.getKey(), entry.getValue());
        }
        return ABSTRACT_ACTION;
    }

    /**
     * setSession
     */
    public static AbstractAction setSession(String key, Object value) {
        getSession().setAttribute(key, value);
        return ABSTRACT_ACTION;
    }

    /**
     * getSession
     */
    public static Object getSession(String key) {
        return getSession().getAttribute(key);
    }

    /**
     * getSession
     */
    public static <C> C getSession(Class<C> type, String key) {
        return Convert.toType(type, getSession(key));
    }

    /**
     * getParameterValues
     */
    public static String[] getParameterValues(String key) {
        return getRequest().getParameterValues(key);
    }

    /**
     * getParameterValues
     */
    public static <C> C[] getParameterValues(String key, Class<C> type) {
        return Convert.toType(type, getRequest().getParameterValues(key));
    }

    /**
     * getCookieValue
     */
    public String getCookieValue(String name, String defaultValue) {
        Cookie cookie = getCookie(name);
        return cookie != null ? cookie.getValue() : defaultValue;
    }

    /**
     * getCookieValue
     */
    public String getCookieValue(String name) {
        return getCookieValue(name, null);
    }

    /**
     * getCookie
     */
    public Cookie getCookie(String name) {
        Cookie[] cookies = getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * getCookies
     */
    public Cookie[] getCookies() {
        return getRequest().getCookies();
    }

    /**
     * setCookie
     */
    public static AbstractAction setCookie(Cookie cookie) {
        getResponse().addCookie(cookie);
        return ABSTRACT_ACTION;
    }

    /**
     * setCookie
     */
    public static AbstractAction setCookie(String name, String value, int maxAgeInSeconds, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath(path);
        getResponse().addCookie(cookie);
        return ABSTRACT_ACTION;
    }

    /**
     * setCookie
     */
    public static AbstractAction setCookie(String name, String value, int maxAgeInSeconds, String path, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath(path);
        getResponse().addCookie(cookie);
        return ABSTRACT_ACTION;
    }

    /**
     * setCookie
     */
    public static AbstractAction setCookie(String name, String value, int maxAgeInSeconds) {
        setCookie(name, value, maxAgeInSeconds, "/");
        return ABSTRACT_ACTION;
    }

    /**
     * removeCookie
     */
    public static AbstractAction removeCookie(String name) {
        setCookie(name, null, 0, "/");
        return ABSTRACT_ACTION;
    }

    /**
     * removeCookie
     */
    public static AbstractAction removeCookie(String name, String path) {
        setCookie(name, null, 0, path);
        return ABSTRACT_ACTION;
    }
}