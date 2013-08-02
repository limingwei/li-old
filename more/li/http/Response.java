package li.http;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * HTTP响应
 * 
 * @author 明伟
 */
public class Response {
    private HttpURLConnection httpURLConnection;

    /**
     * 正文类容
     */
    private byte[] data;

    /**
     * 正文文本
     */
    private String body;

    /**
     * 包括RequestcCookies和ResponseCookies
     */
    private List<HttpCookie> cookies;

    private Map<String, List<String>> headers;

    /**
     * 设置Cookie，在request里有使用
     */
    public void setCookies(List<HttpCookie> cookies) {
        this.cookies = cookies;
    }

    /**
     * 返回Cookie列表
     */
    public List<HttpCookie> getCookies() {
        return this.cookies;
    }

    public HttpCookie getCookie(String name) {
        for (HttpCookie cookie : this.cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public HttpURLConnection getHttpURLConnection() {
        return this.httpURLConnection;
    }

    public void setHttpURLConnection(HttpURLConnection httpURLConnection) {
        this.httpURLConnection = httpURLConnection;
    }

    /**
     * 获取response返回的正文类容
     */
    public byte[] getData() {
        if (null == this.data) {
            try {
                this.data = Util.streamToByteArray(this.getHttpURLConnection().getInputStream());
            } catch (Exception e) {
                this.data = new byte[0];
            }
        }
        return this.data;
    }

    /**
     * 文本形式的response正文
     */
    public String getBody() {
        if (null == this.body) {
            this.body = new String(this.getData());
        }
        return this.body;
    }

    public Map<String, List<String>> getHeaders() {
        if (null == this.headers) {
            this.headers = this.getHttpURLConnection().getHeaderFields();
        }
        return this.headers;
    }

    public String getHeader(String name) {
        if (null != this.getHeaders()) {
            List<String> temp = this.getHeaders().get(name);
            if (null != temp && !temp.isEmpty()) {
                return temp.get(0).toString();
            }
        }
        return null;
    }

    /**
     * getResponseCode
     */
    public Integer getStatus() {
        try {
            return this.getHttpURLConnection().getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}