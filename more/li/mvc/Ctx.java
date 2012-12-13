package li.mvc;

import java.util.Properties;

import li.util.Files;
import li.util.Log;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Ctx extends Context {
    private static final Log log = Log.init();

    public static String freemarker(String path) {
        try {
            Configuration configuration = (Configuration) Log.get("freemarkerConfiguration"); // 从缓存中查找freemarkerTemplate
            if (null == configuration) { // 缓存中没有
                log.debug("freemarker initializing ..");
                configuration = new Configuration();// 初始化freemarkerTemplate
                configuration.setServletContextForTemplateLoading(getServletContext(), "/");// 设置模板加载跟路径
                Properties properties = new Properties();// 默认的参数设置
                properties.put("default_encoding", "UTF-8");
                properties.putAll(Files.load("freemarker.properties"));// freemarker.properties中的参数设置
                configuration.setSettings(properties);
                Log.put("freemarkerConfiguration", configuration); // 缓存freemarkerTemplate
            }
            Template template = configuration.getTemplate(path);// 加载模板
            template.process(getAttributes(), getResponse().getWriter());
            log.info("freemarker to: " + path);
        } catch (Throwable e) {
            error(e);
        }
        return "~!@#DONE";
    }
}