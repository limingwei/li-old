package li.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import li.dao.Page;
import li.model.Action;
import li.model.Field;
import li.util.Convert;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;
import li.util.Verify;

/**
 * MVC的Filter,负责分发HTTP请求
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.3 (2012-05-08)
 */
public class ActionFilter implements Filter {
    private static final String ENCODING = Files.load("config.properties").getProperty("servlet.encoding", "UTF-8");// Servlet编码,可在配置文件中配置

    private static final String USE_I18N = Files.load("config.properties").getProperty("servlet.i18n", "false");// 是否使用国际化

    Log log = Log.init();

    public void destroy() {}

    /**
     * 初始化Filter,设置一些环境变量,只执行一次
     */
    public void init(FilterConfig config) throws ServletException {
        this._init(config.getServletContext());
    }

    /**
     * 初始化Filter
     */
    void _init(ServletContext servletContext) {
        servletContext.setAttribute("root", servletContext.getContextPath() + "/");// 默认的环境变量
        if ("true".equals(USE_I18N.trim().toLowerCase())) {
            servletContext.setAttribute("lang", Files.load(Locale.getDefault().toString()));// 根据Locale.getDefault()初始化国际化,存到servletContext
            log.info("Setting default language as ?", Locale.getDefault());
        }
    }

    /**
     * 从ActionContext中查询到符合当前请求路径的Action并执行,或者chain.doFilter()
     * 
     * @see li.util.Reflect#invoke(Object, String, Object...)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!this._service((HttpServletRequest) request, (HttpServletResponse) response)) {
            log.info("ACTION NOT FOUND: path=\"?\",method=\"?\"", ((HttpServletRequest) request).getServletPath(), ((HttpServletRequest) request).getMethod());
            chain.doFilter(request, response);
        }
    }

    /**
     * 返回值表示是否有Action受理此请求
     */
    Boolean _service(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding(ENCODING);// 设置编码
        response.setCharacterEncoding(ENCODING);

        if ("true".equals(USE_I18N.trim().toLowerCase())) {
            String lang = request.getParameter("lang");// 根据Parameter参数设置国际化,存到session
            if (!Verify.isEmpty(lang)) {
                ((HttpServletRequest) request).getSession().setAttribute("lang", Files.load(lang));
                log.info("Setting language for ?", lang);
            }
        }
        // 请求路径路由
        Action action = ActionContext.getInstance().getAction(((HttpServletRequest) request).getServletPath(), ((HttpServletRequest) request).getMethod());
        if (null != action) {
            Context.init(request, response, action);// 初始化Context
            log.info("ACTION FOUND: path=\"?\",method=\"?\" action=?()", Context.getRequest().getServletPath(), Context.getRequest().getMethod(), action.actionMethod);

            Object result = Reflect.invoke(action.actionInstance, action.actionMethod, this._args());// 执行Action方法
            if (result instanceof String && !result.equals("~!@#DONE")) {// 返回值为String且未调用视图方法
                Context.view((String) result);// 则Context.view返回视图
            }
            return true;
        }
        return false;
    }

    /**
     * 适配参数
     */
    Object[] _args() {
        Action action = Context.getAction();
        Object[] args = new Object[action.argTypes.length]; // Action方法参数值列表
        for (int i = 0; i < action.argTypes.length; i++) {// Action方法参数适配
            String key = (null == action.argAnnotations[i]) ? action.argNames[i] : action.argAnnotations[i].value();// ParameterKey
            if (Verify.basicType(action.argTypes[i]) && !action.argTypes[i].isArray()) { // 单个基本类型
                args[i] = Convert.toType(action.argTypes[i], Context.getRequest().getParameter(key));
            } else if (Verify.basicType(action.argTypes[i]) && action.argTypes[i].isArray()) { // 基本类型的数组
                args[i] = Context.getArray(action.argTypes[i].getComponentType(), key);
            } else if (ServletRequest.class.isAssignableFrom(action.argTypes[i])) { // Request
                args[i] = Context.getRequest();
            } else if (ServletResponse.class.isAssignableFrom(action.argTypes[i])) { // Response
                args[i] = Context.getResponse();
            } else if (Page.class.isAssignableFrom(action.argTypes[i])) { // Page
                args[i] = Context.getPage(key);
            } else if (Field.list(action.argTypes[i], false).size() > 0 || Map.class.isAssignableFrom(action.argTypes[i])) {
                key = (null == action.argAnnotations[i]) ? action.argNames[i] + "." : action.argAnnotations[i].value();
                args[i] = Context.get(action.argTypes[i], key);// 数据对象,POJO,如果没加@Arg注解,则key为参数名+"."
            }
        }
        return args;
    }
}