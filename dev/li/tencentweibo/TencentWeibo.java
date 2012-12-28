package li.tencentweibo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TencentWeibo implements Config {

    /**
     * 登陆,获取code和openid
     * 
     * @param username
     * @param password
     */
    public Map<String, String> authorize(String username, String password) {
        String url = "https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id=" + //
                APP_KEY + //
                "&response_type=" + //
                "code" + //
                "&redirect_uri=" + //
                REDIRECT_URI + //
                "&wap=" + //
                "2";

        Document document = Jsoup.parse(content(execute(CLIENT, new HttpGet(url)).getEntity()));

        Map<String, String> map = new HashMap<String, String>();

        map.put("client_id", document.select("#client_id").attr("value"));
        map.put("response_type", document.select("#response_type").attr("value"));
        map.put("redirect_uri", document.select("#redirect_uri").attr("value"));
        map.put("checkType", document.select("#checkType").attr("value"));
        map.put("checkStatus", document.select("#checkStatus").attr("value"));
        map.put("sessionKey", document.select("#sessionKey").attr("value"));
        map.put("state", document.select("#state").attr("value"));
        map.put("sid", document.select("#sid").attr("value"));
        map.put("flag", document.select("#flag").attr("value"));
        map.put("mobile", document.select("#mobile").attr("value"));
        map.put("wap", document.select("#wap").attr("value"));

        map.put("u", username);
        map.put("p", password);

        HttpPost post = new HttpPost(document.select("#loginform").attr("action"));
        post.addHeader("Referer", url);
        post.setEntity(urlEncodedFormEntity(map, UTF8));

        String response = content(execute(CLIENT, post).getEntity());
        Map<String, String> result = new HashMap<String, String>();
        if (response.length() > 250) {
            result.put("error", "密码错误");
        } else {
            result.put("code", response.substring(response.indexOf("code=") + 5, response.indexOf("code=") + 37));
            result.put("openid", response.substring(response.indexOf("openid=") + 7, response.indexOf("openid=") + 39));
        }
        return result;
    }

    /**
     * 用code换取access_token
     */
    public String access_token(String code) {
        String url = "https://open.t.qq.com/cgi-bin/oauth2/access_token?client_id=" + //
                APP_KEY + //
                "&client_secret=" + //
                APP_SECRET + //
                "&redirect_uri=" + //
                REDIRECT_URI + //
                "&grant_type=" + //
                "authorization_code" + //
                "&code=" + //
                code;

        String response = content(execute(CLIENT, new HttpPost(url)).getEntity());
        return response.substring(response.indexOf("access_token=") + 13, response.indexOf("access_token=") + 45);// 返回access_token
    }

    /**
     * 发布一条微博
     */
    public String addWeibo(String access_token, String open_id, String content) {
        String url = "https://open.t.qq.com/api/t/add";
        String ip = "220.181.111.85";

        Map<String, String> map = new HashMap<String, String>();
        map.put("oauth_consumer_key", APP_KEY);
        map.put("access_token", access_token);
        map.put("openid", open_id);
        map.put("clientip", ip);
        map.put("oauth_version", "2.a");
        map.put("scope", "all");
        map.put("format", "json");
        map.put("content", content);

        HttpPost post = new HttpPost(url);
        post.setEntity(urlEncodedFormEntity(map, UTF8));

        String response = content(execute(CLIENT, post).getEntity());
        return response;// {"data":{"id":"209949107254015","time":1356685040},"errcode":0,"msg":"ok","ret":0,"seqid":5826917877778055590}
    }

    /**
     * 组装http请求的form表单
     */
    private static HttpEntity urlEncodedFormEntity(Map<String, String> map, String charset) {
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (Entry<String, String> each : map.entrySet()) {
            formParams.add(new BasicNameValuePair(each.getKey(), each.getValue()));
        }
        try {
            return new UrlEncodedFormEntity(formParams, charset);
        } catch (UnsupportedEncodingException e) {
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