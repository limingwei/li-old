package li.template.demo;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import li.template.Engine;
import li.template.Template;
import li.util.Convert;

//httl.spi.parsers.AbstractParser.parseClass(Resource, boolean, int)

public class Demo {
    public static void main(String[] args) {
        Engine engine = Engine.getIntense(Convert.toMap("TEMPLATE_DIRECTORY", "E:/workspace/li/dev/li/template/demo/"));
        Template template = engine.getTemplate("index.htm");

        Map params = new HashMap();
        params.put("name", "limingwei");
        params.put("flag", true);
        template.render(params, new OutputStreamWriter(System.out));
    }
}