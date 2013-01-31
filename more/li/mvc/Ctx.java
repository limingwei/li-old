package li.mvc;

import httl.Engine;
import httl.Template;

import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import li.util.Convert;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;

public class Ctx {
    private static final Log log = Log.init();

    /**
     * 主视图方法,以冒号分割前缀表示视图类型
     * 
     * @see li.mvc.Context#forward(String)
     * @see li.mvc.Context#freemarker(String)
     * @see li.mvc.Context#redirect(String)
     * @see li.mvc.Context#write(String)
     * @see #velocity(String)
     * @see #beetl(String)
     * @see #httl(String)
     */
    public static String view(String path) {
        String viewType = path.contains(":") ? path.split(":")[0] : Context.VIEW_TYPE;// 视图类型
        String viewPath = path.startsWith(viewType + ":") ? path.split(viewType + ":")[1] : path;// path冒号后的部分或者path
        if ("forward".equals(viewType) || "fw".equals(viewType)) {// forward视图
            Context.forward(Context.VIEW_PREFIX + viewPath + Context.VIEW_SUFFIX);
        } else if ("freemarker".equals(viewType) || "fm".equals(viewType)) {// freemarker视图
            Context.freemarker(Context.VIEW_PREFIX + viewPath + Context.VIEW_SUFFIX);
        } else if ("velocity".equals(viewType) || "vl".equals(viewType)) {// velocity视图
            velocity(Context.VIEW_PREFIX + viewPath + Context.VIEW_SUFFIX);
        } else if ("beetl".equals(viewType) || "bt".equals(viewType)) {// beetl视图
            beetl(Context.VIEW_PREFIX + viewPath + Context.VIEW_SUFFIX);
        } else if ("httl".equals(viewType) || "ht".equals(viewType)) {// beetl视图
            httl(Context.VIEW_PREFIX + viewPath + Context.VIEW_SUFFIX);
        } else if ("redirect".equals(viewType) || "rd".equals(viewType)) {// redirect跳转
            Context.redirect(viewPath);
        } else if ("write".equals(viewType) || "wt".equals(viewType)) {// 向页面write数据
            Context.write(viewPath);
        } else {
            throw new RuntimeException("view error, not supported viewtype: " + viewType);
        }
        return "~!@#DONE";
    }

    /**
     * 返回beetl视图
     */
    public static String beetl(String path) {
        try {
            Object groupTemplate = Log.get("groupTemplate");// 从缓存中查找GroupTemplate
            if (null == groupTemplate) {
                log.debug("beetl initializing ...");
                Object config = Reflect.born("org.bee.tl.core.Config");// 加载默认配置
                Reflect.invoke(config, "put", "TEMPLATE_ROOT", Context.getServletContext().getRealPath("/"));
                Reflect.invoke(config, "put", "TEMPLATE_CHARSET", "UTF-8");
                Properties properties = Files.load("beetl.properties");// 加载自定义配置,覆盖默认
                for (Entry<Object, Object> entry : properties.entrySet()) {
                    Reflect.invoke(config, "put", entry.getKey().toString(), entry.getValue().toString());
                }
                groupTemplate = Reflect.invoke(config, "createGroupTemplate");// 生成GroupTemplate,并缓存之
                Log.put("groupTemplate", groupTemplate);
            }
            Object template = Reflect.invoke(groupTemplate, "getFileTemplate", path);// 生成模板
            Set<Entry<String, Object>> attributes = Context.getAttributes().entrySet();
            for (Entry<String, Object> entry : attributes) {
                Reflect.invoke(template, "set", new Class[] { String.class, Object.class }, new Object[] { entry.getKey(), entry.getValue() });// 设置变量
            }
            Reflect.invoke(template, "getText", new Class[] { Writer.class }, new Object[] { Context.getResponse().getWriter() });// merge 模板和模型，将内容输出到Writer里
            log.info("forword to beetl: ?", path);
        } catch (Throwable e) {
            Context.error(e);
        }
        return "~!@#DONE";
    }

    /**
     * 返回velocity视图
     */
    public static String velocity(String path) {
        try {
            Object initialized = Log.get("velocityInitialized"); // 从缓存中查找velocity是否初始化的标记
            if (null == initialized) { // 缓存中没有
                log.debug("velocity initializing ..");
                Properties properties = new Properties();// 默认的参数设置
                properties.put("file.resource.loader.path", Context.getServletContext().getRealPath("/"));
                properties.put("input.encoding", "UTF-8");
                properties.put("output.encoding", "UTF-8");
                properties.putAll(Files.load("velocity.properties"));// velocity.properties中的参数设置
                Reflect.call("org.apache.velocity.app.Velocity", "init", properties);// 初始化Velocity
                Log.put("velocityInitialized", true); // 设置velocityInitialized标记
            }
            Object context = Reflect.born("org.apache.velocity.VelocityContext", new Class[] { Map.class }, new Object[] { Context.getAttributes() });// velocity值栈
            Object template = Reflect.call("org.apache.velocity.app.Velocity", "getTemplate", path);// velocity模板
            Reflect.invoke(template, "merge", new Class[] { Reflect.getType("org.apache.velocity.context.Context"), Reflect.getType("java.io.Writer") }, new Object[] { context, Context.getResponse().getWriter() });
            log.info("velocity to: ?", path);
        } catch (Throwable e) {
            Context.error(e);
        }
        return "~!@#DONE";
    }

    /**
     * 返回HTTL视图
     */
    public static String httl(String path) {
        try {
            Engine engine = (Engine) Log.get("httl_engine");
            if (null == engine) {
                Properties properties = new Properties();
                properties.put("loaders", "httl.spi.loaders.FileLoader");
                properties.put("template.directory", System.getProperty("user.dir") + "/dev/li/httl/");
                properties.put("template.suffix", ".htm");
                engine = Engine.getEngine(properties);
            }
            Template template = engine.getTemplate("httl.htm");
            template.render(Context.getAttributes(), Context.getResponse().getWriter());
        } catch (Exception e) {
            Context.error(e);
        }
        return "~!@#DONE";
    }

    /**
     * 
     */
    public static String getServletPath() {
        return Context.getRequest().getServletPath();
    }

    /**
     * 
     */
    public static Map<String, String[]> getParameterMap() {
        return Context.getRequest().getParameterMap();
    }

    /**
     * 
     */
    public static void setSession(String key, Object value) {
        Context.getSession().setAttribute(key, value);
    }

    /**
     * 
     */
    public static Object getSession(String key) {
        return Context.getSession().getAttribute(key);
    }

    /**
     * 
     */
    public static <C> C getSession(Class<C> type, String key) {
        return Convert.toType(type, getSession(key));
    }

    /**
     * 
     */
    public static Object getParameter(String key) {
        return Context.getRequest().getParameter(key);
    }

    /**
     * 
     */
    public static <C> C getParameter(Class<C> type, String key) {
        return Convert.toType(type, getParameter(key));
    }
}