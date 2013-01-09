package li.template;

import java.io.Writer;
import java.util.Map;

/**
 * @author li
 */
public abstract class Template {
    public void render(Map params, Writer writer) {
        try {
            doRender(params, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void doRender(Map params, Writer writer) throws Exception;
}