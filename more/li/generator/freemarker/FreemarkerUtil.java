package li.generator.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import li.generator.Config;
import li.util.Log;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author 明伟
 */
public class FreemarkerUtil {
    private static final Log log = Log.init();

    public static void generateFile(Config config, File temp, Map values) throws Throwable {
        log.debug("li.generator.FreemarkerUtil.generateFile(?, ?, ?)", config, temp, values);

        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(new FileTemplateLoader(new File(temp.getParent())));
        Template template = configuration.getTemplate(temp.getName());
        Map map = new HashMap();
        map.putAll(values);
        File file = new File(values.get("outputDir") + "//" + config.getOutPutFileName((String) values.get("templateDir"), config.getConfigMap(), temp, config.getEntityName((String) values.get("tableName"))));
        file.getParentFile().mkdirs();
        template.process(map, new FileWriter(file));
        log.debug("已经生成文件 " + file.getCanonicalPath());
    }
}