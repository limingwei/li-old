package li.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
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
 * 负责分发HTTP请求
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.3 (2012-05-08)
 */
public class ActionServlet extends HttpServlet {
    private static final long serialVersionUID = -8214041532098707839L;

    private static final Log log = Log.init();

    private static final String DEV_MODE = Files.config().getProperty("devMode", "false");// 是否开发模式,开发模式才会将异常信息展示到页面
    private static final String ENCODING = Files.config().getProperty("servlet.encoding", "UTF-8");// Servlet编码,可在配置文件中配置
    private static final String USE_I18N = Files.config().getProperty("servlet.i18n", "false");// 是否使用国际化

    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        servletContext.setAttribute("root", servletContext.getContextPath() + "/");// 默认的环境变量
        if ("true".equals(USE_I18N.trim().toLowerCase())) {
            servletContext.setAttribute("lang", Files.load(Locale.getDefault().toString()));// 根据Locale.getDefault()初始化国际化,存到servletContext
            log.info("Setting default language as ?", Locale.getDefault());
        }
    }

    /**
     * 处理请求
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try { // 请求路径路由
            Action action = ActionContext.getInstance().getAction(((HttpServletRequest) request).getServletPath(), ((HttpServletRequest) request).getMethod());
            if (null != action) {
                this.pre(request, response);// 设置字符编码并处理国际化

                Context.init(request, response, action);// 初始化Context
                log.info("ACTION FOUND: path=\"?\",method=\"?\" action=?()", Context.getRequest().getServletPath(), Context.getRequest().getMethod(), action.actionMethod);

                Object result = Reflect.invoke(action.actionInstance, action.actionMethod, this.args());// 执行Action方法
                if (result instanceof String && !result.equals("~!@#DONE")) {// 返回值为String且未调用视图方法
                    Context.view((String) result);// 则Context.view返回视图
                }
            }
            log.info("ACTION NOT FOUND: path=\"?\",method=\"?\"", ((HttpServletRequest) request).getServletPath(), ((HttpServletRequest) request).getMethod());
        } catch (Throwable e) {
            if ("true".equalsIgnoreCase(DEV_MODE)) {
                throw new RuntimeException(e + " ", e);
            } else {
                response.setStatus(500);
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置字符编码并处理国际化
     */
    private void pre(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding(ENCODING);// 设置编码
        response.setCharacterEncoding(ENCODING);
        if ("true".equalsIgnoreCase(USE_I18N)) {
            String lang = request.getParameter("lang");// 根据Parameter参数设置国际化,存到session
            if (!Verify.isEmpty(lang)) {
                request.getSession().setAttribute("lang", Files.load(lang));
                log.info("Setting language for ?", lang);
            }
        }
    }

    /**
     * 适配参数
     */
    private Object[] args() {
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