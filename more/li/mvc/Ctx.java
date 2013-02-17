package li.mvc;

import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.Cookie;

import li.util.Convert;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;

/**
 * Action工具类
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-02-15)
 * @see li.mvc.Context
 */
public class Ctx extends Context {
    private static final Log log = Log.init();

    private static final AbstractAction ABSTRACT_ACTION = new AbstractAction() {};

    /**
     * 主视图方法,以冒号分割前缀表示视图类型
     * 
     * @see #velocity(String)
     * @see #beetl(String)
     * @see #httl(String)
     * @see li.mvc.Context#forward(String)
     * @see li.mvc.Context#freemarker(String)
     * @see li.mvc.Context#redirect(String)
     * @see li.mvc.Context#write(String)
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
            return view(path);
        }
    }

    /**
     * 返回beetl视图
     */
    public static String beetl(String path) {
        try {
            Object groupTemplate = Log.get("group_template");// 从缓存中查找GroupTemplate
            if (null == groupTemplate) {
                log.debug("beetl initializing ...");
                Properties properties = new Properties();
                properties.put("TEMPLATE_ROOT", getRootPath());
                properties.put("TEMPLATE_CHARSET", "UTF-8");
                properties.putAll(Files.load("beetl.properties"));// 加载自定义配置,覆盖默认
                Object config = Reflect.born("org.bee.tl.core.Config");// 加载默认配置
                Reflect.invoke(config, "put", new Class<?>[] { Map.class }, new Object[] { properties });
                groupTemplate = Reflect.invoke(config, "createGroupTemplate");// 生成GroupTemplate,并缓存之
                Log.put("group_template", groupTemplate);
            }
            Object template = Reflect.invoke(groupTemplate, "getFileTemplate", path);// 生成模板
            Map<String, Object> attributes = getAttributes();
            for (Entry<String, Object> entry : attributes.entrySet()) {
                Reflect.invoke(template, "set", new Class[] { String.class, Object.class }, new Object[] { entry.getKey(), entry.getValue() });// 设置变量
            }
            Reflect.invoke(template, "getText", new Class[] { Writer.class }, new Object[] { getResponse().getWriter() });// merge 模板和模型，将内容输出到Writer里
            log.info("beetl to : ?", path);
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
            Object initialized = Log.get("velocity_initialized"); // 从缓存中查找velocity是否初始化的标记
            if (null == initialized) { // 缓存中没有
                log.debug("velocity initializing ..");
                Properties properties = new Properties();// 默认的参数设置
                properties.put("file.resource.loader.path", getRootPath());
                properties.put("input.encoding", "UTF-8");
                properties.put("output.encoding", "UTF-8");
                properties.putAll(Files.load("velocity.properties"));// velocity.properties中的参数设置
                Reflect.call("org.apache.velocity.app.Velocity", "init", properties);// 初始化Velocity
                Log.put("velocity_initialized", true); // 设置velocityInitialized标记
            }
            Object context = Reflect.born("org.apache.velocity.VelocityContext", new Class[] { Map.class }, new Object[] { getAttributes() });// velocity值栈
            Object template = Reflect.call("org.apache.velocity.app.Velocity", "getTemplate", path);// velocity模板
            Reflect.invoke(template, "merge", new Class[] { Reflect.getType("org.apache.velocity.context.Context"), java.io.Writer.class }, new Object[] { context, getResponse().getWriter() });
            log.info("velocity to : ?", path);
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
            Object engine = Log.get("httl_engine");
            if (null == engine) {
                log.debug("httl initializing ..");
                Properties properties = new Properties();
                properties.put("loaders", "httl.spi.loaders.FileLoader");
                properties.put("template.directory", getRootPath());
                properties.put("template.suffix", ".htm");
                properties.put("input.encoding", "UTF-8");
                properties.putAll(Files.load("httl.properties"));// httl.properties中的参数设置
                engine = Reflect.call("httl.Engine", "getEngine", properties);
                Log.put("httl_engine", engine);
            }
            Object template = Reflect.invoke(engine, "getTemplate", path);
            Reflect.invoke(template, "render", new Class<?>[] { Object.class, Object.class }, new Object[] { getAttributes(), getResponse().getWriter() });
            log.info("httl to : ?", path);
        } catch (Exception e) {
            error(e);
        }
        return "~!@#DONE";
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

    /**
     * setLocaleToCookie
     */
    public static AbstractAction setLocaleToCookie(Locale locale) {
        // setCookie(I18N_LOCALE, locale.toString(), I18N.getI18nMaxAgeOfCookie());
        return ABSTRACT_ACTION;
    }

    /**
     * setLocaleToCookie
     */
    public static AbstractAction setLocaleToCookie(Locale locale, int maxAge) {
        // setCookie(I18N_LOCALE, locale.toString(), maxAge);
        return ABSTRACT_ACTION;
    }

    /**
     * getText
     */
    public String getText(String key) {
        return "I18N.getText(key, getLocaleFromCookie())";
    }

    /**
     * getText
     */
    public String getText(String key, String defaultValue) {
        return "I18N.getText(key, defaultValue, getLocaleFromCookie())";
    }
}