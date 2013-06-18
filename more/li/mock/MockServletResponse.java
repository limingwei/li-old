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

    private Locale locale = Locale.getDefault();

    private String characterEncoding = "UTF-8";

    private String contentType;

    public PrintWriter getWriter() throws IOException {
        log.info("li.mock.MockServletResponse.getWriter() calling by " + Tool.stackTrace());

        return new PrintWriter(System.out);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        log.info("li.mock.MockServletResponse.getOutputStream() calling by " + Tool.stackTrace());

        return new ServletOutputStream() {
            public void write(int b) throws IOException {
                getWriter().write(b);
            }
        };
    }

    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBufferSize(int bufferSize) {}

    public void setContentLength(int contentLength) {}

    public int getBufferSize() {
        return 0;
    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {}

    public void resetBuffer() {}

    public void flushBuffer() throws IOException {}
}