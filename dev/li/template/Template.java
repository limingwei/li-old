package li.template;

import java.io.Writer;
import java.util.Map;

public class Template {
    public void render(Map params, Writer writer) {
        try {
            for (int i = 0; i < 10; i++) {
                writer.write(i + "\t");
            }
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}