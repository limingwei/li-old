package li.mvc;

import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

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
        if ("velocity".equals(viewType) || "vl".equals(viewType)) {// velocity视图
            return velocity(Context.VIEW_PREFIX + viewPath + Context.VIEW_SUFFIX);
        } else if ("beetl".equals(viewType) || "bt".equals(viewType)) {// beetl视图
            return beetl(Context.VIEW_PREFIX + viewPath + Context.VIEW_SUFFIX);
        } else if ("httl".equals(viewType) || "ht".equals(viewType)) {// beetl视图
            return httl(Context.VIEW_PREFIX + viewPath + Context.VIEW_SUFFIX);
        } else {
            return Context.view(path);
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
                properties.put("TEMPLATE_ROOT", Context.getRootPath());
                properties.put("TEMPLATE_CHARSET", "UTF-8");
                properties.putAll(Files.load("beetl.properties"));// 加载自定义配置,覆盖默认
                Object config = Reflect.born("org.bee.tl.core.Config");// 加载默认配置
                Reflect.invoke(config, "put", new Class<?>[] { Map.class }, new Object[] { properties });
                groupTemplate = Reflect.invoke(config, "createGroupTemplate");// 生成GroupTemplate,并缓存之
                Log.put("group_template", groupTemplate);
            }
            Object template = Reflect.invoke(groupTemplate, "getFileTemplate", path);// 生成模板
            Map<String, Object> attributes = Context.getAttributes();
            for (Entry<String, Object> entry : attributes.entrySet()) {
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
            Object initialized = Log.get("velocity_initialized"); // 从缓存中查找velocity是否初始化的标记
            if (null == initialized) { // 缓存中没有
                log.debug("velocity initializing ..");
                Properties properties = new Properties();// 默认的参数设置
                properties.put("file.resource.loader.path", Context.getRootPath());
                properties.put("input.encoding", "UTF-8");
                properties.put("output.encoding", "UTF-8");
                properties.putAll(Files.load("velocity.properties"));// velocity.properties中的参数设置
                Reflect.call("org.apache.velocity.app.Velocity", "init", properties);// 初始化Velocity
                Log.put("velocity_initialized", true); // 设置velocityInitialized标记
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
            Object engine = Log.get("httl_engine");
            if (null == engine) {
                log.debug("httl initializing ..");
                Properties properties = new Properties();
                properties.put("loaders", "httl.spi.loaders.FileLoader");
                properties.put("template.directory", Context.getRootPath());
                properties.put("template.suffix", ".htm");
                properties.put("input.encoding", "UTF-8");
                properties.putAll(Files.load("httl.properties"));// httl.properties中的参数设置
                engine = Reflect.call("httl.Engine", "getEngine", properties);
                Log.put("httl_engine", engine);
            }
            Object template = Reflect.invoke(engine, "getTemplate", path);
            Reflect.invoke(template, "render", new Class<?>[] { Object.class, Object.class }, new Object[] { Context.getAttributes(), Context.getResponse().getWriter() });
            log.info("httl to: ?", path);
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