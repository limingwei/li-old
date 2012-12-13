package li.mvc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import li.util.Log;

public abstract class AbstractView {
    private static final Log log = Log.init();

    public abstract void init();

    public abstract void doView(ServletRequest request, ServletResponse response, Object arg);

    public static String view() {
        return "~!@#DONE";
    }

    /**
     * 视图层异常处理,为了安全,页面上没有异常信息
     */
    protected static void error(Throwable e) {
        Context.getResponse().setStatus(500);
        log.error(e.getMessage());
        e.printStackTrace();
    }
}