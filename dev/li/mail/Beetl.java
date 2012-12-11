package li.mail;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Map.Entry;

import org.bee.tl.core.Config;
import org.bee.tl.core.Template;

public class Beetl {
    private Template template;

    public Beetl(File file) {
        try {
            template = new Config().createGroupTemplate().getReaderTemplate(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String merge(Map<?, ?> map) {
        try {
            for (Entry<?, ?> entry : map.entrySet()) {
                template.set(entry.getKey().toString(), entry.getValue());
            }
            return template.getTextAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}