package li.qq3g;

import java.util.ArrayList;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

public class QQ3G {
    private static final String UTF8 = "UTF-8";
    private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();

    /**
     * 登陆
     */
    public String login(String username, String password) {
        String referer = "http://pt.3g.qq.com/s?aid=nLogin3gqq&auto=1&s_it=1&g_f=286&sid=AfSsoTvRoUqcOGuBitGc2anf";

        String action = "http://pt.3g.qq.com/psw3gqqLogin?r=155089860&amp;vdata=61EC3202AA4BB9CDB7D3068575C6DC30";

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("qq", username));
        formParams.add(new BasicNameValuePair("pwd", password));
        formParams.add(new BasicNameValuePair("toQQchat", "true"));
        formParams.add(new BasicNameValuePair("login_url", "http://pt.3g.qq.com/s?aid=nLoginnew&amp;q_from=3GQQ"));
        formParams.add(new BasicNameValuePair("q_from", ""));
        formParams.add(new BasicNameValuePair("modifySKey", "0"));
        formParams.add(new BasicNameValuePair("loginType", "1"));
        formParams.add(new BasicNameValuePair("aid", "nLoginHandle"));

        HttpPost post = new HttpPost(action);
        post.setHeader("Referer", referer);
        post.setEntity(urlEncodedFormEntity(formParams, UTF8));

        String redirect = content(execute(HTTP_CLIENT, post).getEntity());

        return Jsoup.parse(redirect).select("a").attr("href");
    }

    /**
     * 刷新消息列表页
     */
    public String refreshChat(String sid, String qq) {
        String url = "http://q32.3g.qq.com/g/s?sid=" + sid + "&aid=nqqChat&u=" + qq + "&on=1";
        return content(execute(HTTP_CLIENT, new HttpGet(url)).getEntity());
    }

    /**
     * 发送QQ消息
     */
    public String send(String sid, String msg, String to) {
        String referer = "http://q32.3g.qq.com/g/s?sid=" + sid + "&aid=nqqChat&u=" + to + "&on=1";
        String action = "http://q32.3g.qq.com/g/s?sid=" + sid;

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("msg", msg));
        formParams.add(new BasicNameValuePair("u", to));
        formParams.add(new BasicNameValuePair("saveURL", "0"));
        formParams.add(new BasicNameValuePair("do", "send"));
        formParams.add(new BasicNameValuePair("on", "1"));
        formParams.add(new BasicNameValuePair("saveURL", "0"));
        formParams.add(new BasicNameValuePair("aid", "发送"));
        formParams.add(new BasicNameValuePair("num", to));
        formParams.add(new BasicNameValuePair("do", "sendsms"));

        HttpPost post = new HttpPost(action);
        post.setHeader("Referer", referer);
        post.setEntity(urlEncodedFormEntity(formParams, UTF8));

        return content(execute(HTTP_CLIENT, post).getEntity());
    }

    /**
     * 在线好友列表
     */
    public String online(String sid) {
        String url = "http://q32.3g.qq.com/g/s?sid=" + sid + "&aid=nqqchatMain";
        return content(execute(HTTP_CLIENT, new HttpGet(url)).getEntity());
    }

    /**
     * 组装http请求的form表单
     */
    private static HttpEntity urlEncodedFormEntity(List<NameValuePair> formParams, String charset) {
        try {
            return new UrlEncodedFormEntity(formParams, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行http请求
     */
    private static HttpResponse execute(HttpClient client, HttpUriRequest request) {
        try {
            return client.execute(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提取页面内容文本
     */
    private static String content(HttpEntity entity) {
        try {
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}