package li.tencentweibo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Demo {
    public static void main(String[] args) throws Exception, IOException {
        String ip = "220.181.111.85";

        // authorize();
        String code = "0c4b76e016382c28f944fc455740a1d2";
        String openid = "C149FADBF72D266F998E974F8C1F3B6D";

        // access_token(code);
        String access_token = "89f4c51916faf1af37e3a689d2d81e6d";

        addWeibo(access_token, openid, ip, "床前明月光,测试微博内容");
    }

    /**
     * 出code和openid
     */
    public static void authorize() throws Exception {
        String url = "https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id=" + //
                "801062955" + //
                "&response_type=" + //
                "code" + //
                "&redirect_uri=" + //
                "http://123.cn" + //
                "&wap=" + //
                "2";

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        HttpResponse response = client.execute(get);

        Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()));

        HttpClient client2 = new DefaultHttpClient();
        HttpPost post = new HttpPost(document.select("#loginform").attr("action"));

        post.addHeader("Referer", url);

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("client_id", document.select("#client_id").attr("value")));
        formParams.add(new BasicNameValuePair("response_type", document.select("#response_type").attr("value")));
        formParams.add(new BasicNameValuePair("redirect_uri", document.select("#redirect_uri").attr("value")));
        formParams.add(new BasicNameValuePair("checkType", document.select("#checkType").attr("value")));
        formParams.add(new BasicNameValuePair("checkStatus", document.select("#checkStatus").attr("value")));
        formParams.add(new BasicNameValuePair("sessionKey", document.select("#sessionKey").attr("value")));
        formParams.add(new BasicNameValuePair("state", document.select("#state").attr("value")));
        formParams.add(new BasicNameValuePair("sid", document.select("#sid").attr("value")));
        formParams.add(new BasicNameValuePair("flag", document.select("#flag").attr("value")));
        formParams.add(new BasicNameValuePair("mobile", document.select("#mobile").attr("value")));
        formParams.add(new BasicNameValuePair("wap", document.select("#wap").attr("value")));

        formParams.add(new BasicNameValuePair("u", "1055515958"));
        formParams.add(new BasicNameValuePair("p", "buxiaode"));

        HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");

        post.setEntity(entity);

        HttpResponse response2 = client2.execute(post);

        System.out.println(EntityUtils.toString(response2.getEntity()));// 出code和openid
    }

    public static void access_token(String code) throws Exception {
        String url3 = "https://open.t.qq.com/cgi-bin/oauth2/access_token?client_id=" + //
                "801062955" + //
                "&client_secret=" + //
                "8111a0bb97bf37e5f00ea291ceb89131" + //
                "&redirect_uri=" + //
                "http://123.cn" + //
                "&grant_type=" + //
                "authorization_code" + //
                "&code=" + //
                code;

        HttpClient client3 = new DefaultHttpClient();
        HttpPost post3 = new HttpPost(url3);

        HttpResponse response3 = client3.execute(post3);

        System.out.println(EntityUtils.toString(response3.getEntity()));// 出access_token
    }

    public static void addWeibo(String token, String open_id, String ip, String content) throws Exception {
        String url4 = "https://open.t.qq.com/api/t/add";
        HttpClient client4 = new DefaultHttpClient();
        HttpPost post4 = new HttpPost(url4);
        List<NameValuePair> formParams2 = new ArrayList<NameValuePair>();

        formParams2.add(new BasicNameValuePair("oauth_consumer_key", "801062955"));
        formParams2.add(new BasicNameValuePair("access_token", token));
        formParams2.add(new BasicNameValuePair("openid", open_id));
        formParams2.add(new BasicNameValuePair("clientip", ip));
        formParams2.add(new BasicNameValuePair("oauth_version", "2.a"));
        formParams2.add(new BasicNameValuePair("scope", "all"));

        formParams2.add(new BasicNameValuePair("format", "json"));
        formParams2.add(new BasicNameValuePair("content", content));
        HttpEntity entity2 = new UrlEncodedFormEntity(formParams2, "UTF-8");
        post4.setEntity(entity2);

        HttpResponse response4 = client4.execute(post4);

        System.out.println(EntityUtils.toString(response4.getEntity()));
    }
}