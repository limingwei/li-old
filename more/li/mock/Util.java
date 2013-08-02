package li.mock;

/**
 * MVC MOCK 中用到的 stackTrace 工具
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-06-20)
 */
public class Util {
    private static final String[] SKIP_PACKAGES = { "org.eclipse.jdt.internal.junit", //
            "java.lang.Thread", "java.lang.reflect.Method", //
            "sun.reflect.", //
            "li.mock.Util", "li.mock.Mock",//
            "org.junit.runners.", "org.junit.internal.runners.", //
            "net.sf.cglib.proxy.", //
            "li.aop.AopChain", "li.aop.AopEnhancer" };

    /**
     * 返回当前线程中 stackTrace 列表
     */
    public static String stackTrace() {
        String string = "";
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            if (!skip(elements[i])) {
                string += "\n" + (i + "\t#" + elements[i].getLineNumber() + "\t" + elements[i].getClassName() + "." + elements[i].getMethodName() + "()");
            }
        }
        return string;
    }

    /**
     * 一个StackTraceElement是否应当跳过
     */
    private static Boolean skip(StackTraceElement element) {
        if (element.getLineNumber() < 1) {
            return true;
        }
        for (String header : SKIP_PACKAGES) {
            if (element.getClassName().startsWith(header)) {
                return true;
            }
        }
        return false;
    }
}