package li.generator.template;

import java.io.StringWriter;
import java.util.Map;

import li.generator.resource.Resource;
import li.util.Log;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

public class Template {
    static Log log = Log.init();

    private Resource resource;

    private Map values;

    public Template setResource(Resource resource) {
        this.resource = resource;
        return this;
    }

    public Template setValues(Map values) {
        this.values = values;
        return this;
    }

    /**
     * 返回生成后的内容
     */
    public String make() {
        log.debug("li.generator.template.Template.make() " + this + " " + resource + " " + resource.getBody());
        StringWriter stringWriter = new StringWriter();
        try {
            Configuration configuration = new Configuration();
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", resource.getBody());
            configuration.setTemplateLoader(stringTemplateLoader);
            freemarker.template.Template template = configuration.getTemplate("template");
            template.process(values, stringWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}