package li.weibo;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import weibo4j.Timeline;

/**
 * 微博
 * 
 * @author li
 */
public class SinaWeibo {
    /**
     * @param username
     * @param password
     * @return code
     */
    public String authorize(String username, String password) throws Exception {
        String url = "https://api.weibo.com/oauth2/authorize?client_id=" + //
                Config.client_id + //
                "&response_type=code&redirect_uri=" + //
                Config.redirect_uri + //
                "&display=wap2.0";

        Document document = jsoupGet(Jsoup.connect(url));

        Map<String, String> map = new HashMap<String, String>();
        map.put("display", getByName(document, "display"));
        map.put("action", getByName(document, "action"));
        map.put("response_type", getByName(document, "response_type"));
        map.put("regCallback", getByName(document, "regCallback"));
        map.put("redirect_uri", getByName(document, "redirect_uri"));
        map.put("client_id", getByName(document, "client_id"));
        map.put("state", getByName(document, "state"));
        map.put("from", getByName(document, "from"));
        map.put("with_cookie", getByName(document, "with_cookie"));

        map.put("userId", username);
        map.put("passwd", password);
        map.put("action", "submit");

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://api.weibo.com/oauth2/authorize");
        post.addHeader("Referer", url);

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();

        for (Entry<String, String> arg : map.entrySet()) {
            formParams.add(new BasicNameValuePair(arg.getKey(), arg.getValue()));
        }

        HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");

        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        String location = response.getFirstHeader("Location").getValue();

        return location.substring(location.lastIndexOf("=") + 1);
    }

    /**
     * @param code
     * @return access_token
     */
    public String access_token(String code) throws Exception {
        String url = "https://api.weibo.com/oauth2/access_token?client_id=" + //
                Config.client_id + //
                "&client_secret=" + //
                Config.client_secret + //
                "&grant_type=" + //
                "authorization_code" + //
                "&redirect_uri=" + //
                Config.redirect_uri + //
                "&code=" + //
                code;

        Document document = Jsoup.connect(url).post();
        String response = document.toString();
        return response.substring(response.indexOf("access_token") + 25, response.indexOf("remind_in") - 13);
    }

    /**
     * @param access_token
     * @param status
     * @return Status
     */
    public String update(String token, String status) throws Exception {
        Timeline timeline = new Timeline();
        timeline.setToken(token);
        return timeline.UpdateStatus(status).toString();
    }

    /**
     * @param access_token
     * @param weibo_id
     * @param status
     * @param is_comment 0：不评论、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0 。
     * @return
     */
    public String repost(String access_token, String weibo_id, String status, Integer is_comment) throws Exception {
        String url = "https://api.weibo.com/2/statuses/repost.json";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();

        formParams.add(new BasicNameValuePair("access_token", access_token));
        formParams.add(new BasicNameValuePair("id", weibo_id));
        formParams.add(new BasicNameValuePair("status", status));
        formParams.add(new BasicNameValuePair("is_comment", is_comment.toString()));

        HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");

        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        return EntityUtils.toString(response.getEntity());
    }

    private String getByName(Document document, String query) {
        return document.select("input[name=" + query + "]").attr("value");
    }

    private Document jsoupGet(Connection connection) {
        try {
            return connection.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}