package li.tencentweibo;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public interface Config {
    public static final String APP_KEY = "801062955";
    public static final String APP_SECRET = "8111a0bb97bf37e5f00ea291ceb89131";
    public static final String UTF8 = "UTF-8";
    public static final HttpClient CLIENT = new DefaultHttpClient();
    public static final String REDIRECT_URI = "http://123.cn";
}