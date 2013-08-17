package li.mvc.view;

import httl.Engine;
import httl.Template;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import li.mvc.Context;
import li.util.Files;
import li.util.Log;

/**
 * httl视图
 * 
 * @author : 明伟 
 */
public class HttlView extends AbstractView {
    private static final Log log = Log.init();

    private static Engine engine = null;

    public static synchronized Engine getEngine() {
        if (null == engine) {
            log.debug("httl initializing ..");
            Properties properties = new Properties();
            properties.put("loaders", "httl.spi.loaders.FileLoader");
            properties.put("template.directory", Context.getRootPath());
            properties.put("input.encoding", "UTF-8");
            properties.putAll(Files.load("httl.properties"));// httl.properties中的参数设置
            engine = Engine.getEngine(properties);
        }
        return engine;
    }

    public void render(String tempPath, HttpServletResponse response, Map<String, Object> map) throws Exception {
        Template template = getEngine().getTemplate(tempPath);
        template.render(map, response.getWriter());
        log.info("httl to : ?", tempPath);
    }
}

class HttlMethods {
    public static String left(Object self, int len) {
        return left(self + "", len);
    }

    public static String left(String text, int len) {
        if (text.length() <= len) {
            return text;
        }
        return text.substring(0, len);
    }

    public static String right(Object self, int len) {
        return right(self + "", len);
    }

    public static String right(String text, int len) {
        if (text.length() <= len) {
            return text;
        }
        return text.substring(text.length() - len, text.length());
    }
}