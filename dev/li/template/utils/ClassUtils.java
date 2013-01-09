package li.template.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author li
 */
public class ClassUtils {
    public static URI toURI(String name) {
        try {
            return new URI(name);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
