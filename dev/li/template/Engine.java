package li.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Engine {
    private static final Map<String, Template> TEMPLATES = new ConcurrentHashMap<String, Template>();

    private static final Map<String, String> CONFIG = new ConcurrentHashMap<String, String>();

    public static Engine getIntense() {
        return new Engine();
    }

    public static Engine getIntense(Map config) {
        CONFIG.putAll(config);
        return getIntense();
    }

    private Engine() {

    }

    public Template getTemplate(String name) {
        Template template = TEMPLATES.get(name);
        if (null == template) {
            template = parse(name);
            TEMPLATES.put(name, template);
        }
        return template;
    }

    private Template parse(String name) {
        String content = name + "";
        return new Template();
    }
}