package li.renren;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class Renren implements Config {
    HttpClient HTTP_CLIENT = new DefaultHttpClient();
    HttpContext CONTEXT = new BasicHttpContext();

    public String authorize() {
        String url = "https://graph.renren.com/oauth/authorize?client_id=" + APP_ID //
                + "&redirect_uri=" + CALLBACK_URL //
                + "&response_type=code";

        HttpGet get = new HttpGet(url);

        System.out.println(content(execute(HTTP_CLIENT, get, CONTEXT).getEntity()));

        // browse(url);

        String CODE = "YUTjopva4Aq2f8fubyheb1TGq64fkN8E";
        return CODE;
    }

    public void token(String code) {
        String url = "https://graph.renren.com/oauth/token?grant_type=authorization_code&client_id=" + APP_ID//
                + "&redirect_uri=" + CALLBACK_URL//
                + "&client_secret=" + SECRET_KEY//
                + "&code=" + code;

        HttpPost post = new HttpPost(url);

        System.out.println(content(execute(HTTP_CLIENT, post, CONTEXT).getEntity()));
    }

    public void browse(String url) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI(url);
            desktop.browse(uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行http请求
     */
    public static HttpResponse execute(HttpClient client, HttpUriRequest request, HttpContext context) {
        try {
            return client.execute(request, context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 组装http请求的form表单
     */
    public static HttpEntity urlEncodedFormEntity(List<NameValuePair> formParams, String charset) {
        try {
            return new UrlEncodedFormEntity(formParams, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提取页面内容文本
     */
    public static String content(HttpEntity entity) {
        try {
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}