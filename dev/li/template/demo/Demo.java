package li.template.demo;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import li.template.Engine;
import li.template.Template;
import li.util.Convert;

public class Demo {
    public static void main(String[] args) {
        Engine engine = Engine.getIntense(Convert.toMap("TEMPLATE_DIRECTORY", "D:/workspace/li/dev/li/template/demo/"));
        Template template = engine.getTemplate("index.htm");

        Map<Object, Object> params = new HashMap<Object, Object>();
        params.put("name", "limingwei");
        params.put("flag", true);
        template.render(params, new OutputStreamWriter(System.out));
    }
}