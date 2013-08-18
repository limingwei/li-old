package li.template;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import li.template.compiler.DefaultCompiler;
import li.util.IOUtil;

/**
 * @author li
 */
public class Engine {
    private Map<String, Template> templates = new ConcurrentHashMap<String, Template>();

    private Map<Object, Object> config = new ConcurrentHashMap<Object, Object>();

    public static synchronized Engine getIntense(Map<Object, Object> config) {
        Engine engine = new Engine();
        engine.config.putAll(config);
        return engine;
    }

    public Template getTemplate(String name) {
        Template template = templates.get(name);
        if (null == template) {
            template = parseTemplate(name, IOUtil.read(new File(config.get("TEMPLATE_DIRECTORY") + name)));
            templates.put(name, template);
        }
        return template;
    }

    public Template parseTemplate(String name, String content) {
        Parser parser = new Parser();
        parser.setName(name);
        parser.setContent(content);
        parser.parse();

        return bornTemplate(compileTemplate(parser.getName(), parser.getSource()));
    }

    public Class<? extends Template> compileTemplate(String name, String source) {
        try {
            return (Class<? extends Template>) new DefaultCompiler().doCompile(name, source);
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    public Template bornTemplate(Class<? extends Template> type) {
        try {
            return (Template) type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }
}