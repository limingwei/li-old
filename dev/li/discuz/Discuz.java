package li.discuz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Discuz {
    public static final String REFERER = "Referer";
    public static final String UTF8 = "UTF-8";
    public static final HttpClient HTTP_CLIENT = new DefaultHttpClient();
    public static final HttpContext CONTEXT = new BasicHttpContext();
    public static final CookieStore COOKIE_STORE = new BasicCookieStore();

    private String domain;

    public Discuz(String domain) {
        this.domain = domain;
    }

    public List<Cookie> login(String username, String password) {
        String referer = domain + "member.php?mod=logging&action=login&mobile=yes";

        HttpGet get = new HttpGet(referer);
        Document document = Jsoup.parse(content(execute(HTTP_CLIENT, get, CONTEXT).getEntity()));

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("formhash", document.select("input#formhash").attr("value")));
        formParams.add(new BasicNameValuePair("referer", document.select("input#referer").attr("value")));
        formParams.add(new BasicNameValuePair("submit", document.select("input#submit").attr("value")));
        formParams.add(new BasicNameValuePair("loginsubmit", "true"));
        formParams.add(new BasicNameValuePair("questionid", "0"));
        formParams.add(new BasicNameValuePair("answer", ""));
        formParams.add(new BasicNameValuePair("username", username));
        formParams.add(new BasicNameValuePair("password", password));

        String action = domain + document.select("form").attr("action");
        HttpPost post = new HttpPost(action);
        post.setHeader(REFERER, referer);
        post.setEntity(urlEncodedFormEntity(formParams, UTF8));

        CONTEXT.setAttribute(ClientContext.COOKIE_STORE, COOKIE_STORE);
        HttpResponse response = execute(HTTP_CLIENT, post, CONTEXT);

        System.out.println(content(response.getEntity()));

        return COOKIE_STORE.getCookies();
    }

    public void post(Integer fid, String subject, String message) {
        String referer = domain + "forum.php?mod=post&action=newthread&fid=" + fid + "&mobile=yes";

        HttpGet get = new HttpGet(referer);
        Document document = Jsoup.parse(content(execute(HTTP_CLIENT, get, CONTEXT).getEntity()));

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("formhash", document.select("input#formhash").attr("value")));
        formParams.add(new BasicNameValuePair("posttime", document.select("input#posttime").attr("value")));
        formParams.add(new BasicNameValuePair("subject", subject));
        formParams.add(new BasicNameValuePair("message", message));
        formParams.add(new BasicNameValuePair("topicsubmit", "发表帖子"));

        String action = domain + document.select("form#postform").attr("action");
        HttpPost post = new HttpPost(action);
        post.setHeader(REFERER, referer);
        post.setEntity(urlEncodedFormEntity(formParams, UTF8));
        HttpResponse response = execute(HTTP_CLIENT, post, CONTEXT);

        System.out.println(content(response.getEntity()));
    }

    public void reply(Integer tid, String message) {
        String referer = domain + "forum.php?mod=viewthread&tid=" + tid + "&mobile=yes";

        HttpGet get = new HttpGet(referer);
        Document document = Jsoup.parse(content(execute(HTTP_CLIENT, get, CONTEXT).getEntity()));

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("formhash", document.select("input[name=formhash]").attr("value")));
        formParams.add(new BasicNameValuePair("replysubmit", document.select("input#fastpostsubmit").attr("value")));
        formParams.add(new BasicNameValuePair("message", message));

        String action = domain + document.select("form#fastpostform").attr("action");
        HttpPost post = new HttpPost(action);
        post.setHeader(REFERER, referer);
        post.setEntity(urlEncodedFormEntity(formParams, UTF8));
        HttpResponse response = execute(HTTP_CLIENT, post, CONTEXT);

        System.out.println(content(response.getEntity()));
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