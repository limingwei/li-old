package li.mvc.view;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import li.mvc.Context;
import li.util.Files;
import li.util.Log;

import org.bee.tl.core.Config;
import org.bee.tl.core.GroupTemplate;
import org.bee.tl.core.Template;

/**
 * beetl视图
 * 
 * @author : 明伟 
 */
public class BeetlView extends AbstractView {
    private static final Log log = Log.init();

    private static GroupTemplate groupTemplate;

    public static synchronized GroupTemplate getGroupTemplate() {
        if (null == groupTemplate) {
            try {
                log.info("beetl initializing ...");
                Map properties = new Properties();
                properties.put("TEMPLATE_ROOT", Context.getRootPath());
                properties.put("TEMPLATE_CHARSET", "UTF-8");
                properties.putAll(Files.load("beetl.properties"));// 加载自定义配置,覆盖默认
                Config config = new Config();// 加载默认配置
                config.put(properties);
                groupTemplate = config.createGroupTemplate();// 生成GroupTemplate
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return groupTemplate;
    }

    public void render(String tempPath, HttpServletResponse response, Map<String, Object> map) throws Exception {
        Template template = getGroupTemplate().getFileTemplate(tempPath);
        for (Entry<String, Object> entry : map.entrySet()) {
            template.set(entry.getKey(), entry.getValue());
        }
        template.getText(response.getWriter());
        log.debug("beetl to : ?", tempPath);
    }
}
