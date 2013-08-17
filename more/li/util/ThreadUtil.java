package li.util;

/**
 * @author : 明伟 
 */
public class ThreadUtil {
    private static final String[] SKIP_PACKAGES = { "org.eclipse.jdt.internal.junit", //
            "java.lang.Thread", "java.lang.reflect.Method", //
            "sun.reflect.", //
            "li.util.ThreadUtil", "li.mock.Mock",//
            "org.junit.runners.", "org.junit.internal.runners.", //
            "net.sf.cglib.proxy.", //
            "li.aop.AopChain", "li.aop.AopEnhancer" };

    /**
     * 返回当前线程中 stackTrace 列表
     */
    public static String stackTrace() {
        return stackTrace(Thread.currentThread().getStackTrace());
    }

    public static String stackTrace(StackTraceElement[] stackTraceElements) {
        String string = "";
        for (int i = 0; i < stackTraceElements.length; i++) {
            if (!skip(stackTraceElements[i])) {
                string += "\n" + (i + "\t#" + stackTraceElements[i].getLineNumber() + "\t\t" + stackTraceElements[i].getClassName() + "." + stackTraceElements[i].getMethodName() + "()");
            }
        }
        return string;
    }

    public static String stackTrace(StackTraceElement[] stackTraceElements, String regex) {
        String string = "";
        for (int i = 0; i < stackTraceElements.length; i++) {
            String temp = "\n" + (i + "\t#" + stackTraceElements[i].getLineNumber() + "\t\t" + stackTraceElements[i].getClassName() + "." + stackTraceElements[i].getMethodName() + "()");
            if (Verify.regex(temp, regex) && stackTraceElements[i].getLineNumber() > 0) {
                string += temp;
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

    public static void sleep(Number millis) {
        try {
            Thread.sleep(millis.longValue());
        } catch (InterruptedException e) {
            throw new RuntimeException("li.util.ThreadUtil.sleep()", e);
        }
    }
}