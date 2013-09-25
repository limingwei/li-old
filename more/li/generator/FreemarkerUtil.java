package li.generator;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtil {
    public static void generateFile(Config config, String table, File temp) throws Throwable {
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(new FileTemplateLoader(new File(config.getTemplateDir())));
        Template template = configuration.getTemplate(config.getTemplateName(temp));
        Map map = new HashMap();

        File file = new File(config.getOutPutDir() + "//" + config.getOutPutFileName(config.getConfigMap(), temp, config.getEntityName(table)));

        file.getParentFile().mkdirs();
        template.process(map, new FileWriter(file));
    }
}