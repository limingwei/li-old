package li.mvc;

import java.io.Writer;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import li.util.Files;
import li.util.Log;
import li.util.Reflect;

public class FreemarkerView extends AbstractView {
    private static final Log log = Log.init();

    Object configuration = Log.get("freemarkerConfiguration"); // 从缓存中查找freemarkerTemplate

    public void init() {
        if (null == configuration) { // 缓存中没有
            log.debug("freemarker initializing ..");
            configuration = Reflect.born("freemarker.template.Configuration"); // 初始化freemarkerTemplate
            Reflect.invoke(configuration, "setServletContextForTemplateLoading", new Class[] { Object.class, String.class }, new Object[] { Context.getRequest().getServletContext(), "/" });// 设置模板加载跟路径
            Properties properties = new Properties();// 默认的参数设置
            properties.put("default_encoding", "UTF-8");
            properties.putAll(Files.load("freemarker.properties"));// freemarker.properties中的参数设置
            Reflect.invoke(configuration, "setSettings", properties);// 加载自定义配置
            Log.put("freemarkerConfiguration", configuration); // 缓存freemarkerTemplate
        }
    }

    public void doView(ServletRequest request, ServletResponse response, Object arg) {
        try {
            Object template = Reflect.invoke(configuration, "getTemplate", arg);// 加载模板
            Reflect.invoke(template, "process", new Class[] { Object.class, Writer.class }, new Object[] { Context.getAttributes(), response.getWriter() });
            log.info("freemarker to: " + arg);
        } catch (Throwable e) {
            error(e);
        }
    }
}