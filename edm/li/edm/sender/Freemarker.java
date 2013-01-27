package li.edm.sender;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Freemarker {
    private static Template template;

    public Freemarker(String name) {
        try {
            Configuration configuration = new Configuration();
            configuration.setTemplateLoader(new FileTemplateLoader(new File(System.getProperty("user.dir") + "\\dev\\li\\mail\\")));
            Properties properties = new Properties();// 默认的参数设置
            properties.put("default_encoding", "UTF-8");
            template = configuration.getTemplate(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String merge(Map map) {
        try {
            StringWriter writer = new StringWriter();
            template.process(map, writer);
            return writer.getBuffer().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
