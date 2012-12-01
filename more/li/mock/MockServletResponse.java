package li.mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

import li.util.Log;

/**
 * MockServletResponse
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-09-27)
 */
class MockServletResponse implements ServletResponse {
    private static final Log log = Log.init();

    public PrintWriter getWriter() throws IOException {
        log.info("li.mock.MockServletResponse.getWriter() calling by " + Tool.stackTrace());

        return new PrintWriter(System.out) {
            public void write(char[] buf, int off, int len) {}
        };
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public void flushBuffer() throws IOException {}

    public int getBufferSize() {
        return 0;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public Locale getLocale() {
        return null;
    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {}

    public void resetBuffer() {}

    public void setBufferSize(int arg0) {}

    public void setCharacterEncoding(String arg0) {}

    public void setContentLength(int arg0) {}

    public void setContentType(String arg0) {}

    public void setLocale(Locale arg0) {}
}