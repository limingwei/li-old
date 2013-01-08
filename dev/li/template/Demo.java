package li.template;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        Engine engine = Engine.getIntense();
        Template template = engine.getTemplate("index.html");

        Map params = new HashMap();
        template.render(params, new OutputStreamWriter(System.out));
    }
}