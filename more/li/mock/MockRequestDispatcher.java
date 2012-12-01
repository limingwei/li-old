package li.mock;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import li.util.Log;

/**
 * MockRequestDispatcher
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-09-27)
 */
class MockRequestDispatcher implements RequestDispatcher {
    private static final Log log = Log.init();

    private String path;

    public MockRequestDispatcher(String path) {
        this.path = path;
    }

    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        log.info("forward to : " + path + " calling by " + Tool.stackTrace());
    }

    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        log.info("include : " + path + " calling by " + Tool.stackTrace());
    }
}
