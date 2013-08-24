package li.mvc.view;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import li.mvc.Context;
import li.util.Files;
import li.util.Log;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Velocity视图
 * 
 * @author : 明伟 
 */
public class VelocityView extends AbstractView {
    private static final Log log = Log.init();

    private static Object flag;

    public static synchronized void init() {
        if (null == flag) {
            log.info("velocity initializing ..");
            Properties properties = new Properties();// 默认的参数设置
            properties.put("file.resource.loader.path", Context.getRootPath());
            properties.put("input.encoding", "UTF-8");
            properties.put("output.encoding", "UTF-8");
            properties.putAll(Files.load("velocity.properties"));// velocity.properties中的参数设置
            Velocity.init(properties);// 初始化Velocity
            flag = "inited";
        }
    }

    public void render(String tempPath, HttpServletResponse response, Map<String, Object> map) throws Exception {
        init();
        Template template = Velocity.getTemplate(tempPath);
        template.merge(new VelocityContext(map), response.getWriter());
        log.debug("velocity to : ?", tempPath);
    }
}