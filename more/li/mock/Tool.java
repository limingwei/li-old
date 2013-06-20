package li.mock;

public class Tool {
    public static String stackTrace() {
        String string = "";
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            String className = elements[i].getClassName();
            if (elements[i].getLineNumber() > 0 && !className.startsWith("org.eclipse.jdt.internal.") && !className.startsWith("java.lang.") //
                    && !className.startsWith("sun.reflect.") && !className.startsWith("org.junit.") && !className.startsWith("net.sf.cglib.proxy.")//
            /* && !className.startsWith("li.") */) {
                string += "\n" + (i + "\t" + elements[i].getClassName() + "." + elements[i].getMethodName() + "()\t" + "\t# " + elements[i].getLineNumber());
            }
        }
        return string;
    }
}