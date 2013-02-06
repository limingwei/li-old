package li;

import li.ioc.Ioc;
import net.sf.cglib.core.DebuggingClassWriter;

public class T {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\Users\\li\\Desktop\\classes");
        Ioc.get("");
    }
}