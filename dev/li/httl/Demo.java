package li.httl;

import httl.Engine;
import httl.Template;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Demo {
    public static void main(String[] args) throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", "limingwei");

        Properties properties = new Properties();
        properties.put("loaders", "httl.spi.loaders.FileLoader");
        properties.put("template.directory", "E:/workspace/li/dev/li/httl/");
        properties.put("template.suffix", ".htm");

        Engine engine = Engine.getEngine(properties);

        Template template = engine.getTemplate("httl.htm");
        template.render(parameters, System.out);
    }
}