package li.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Demo {
    private static String getByName(Document document, String query) {
        return document.select("input[name=" + query + "]").attr("value");
    }

    public static void main(String[] args) throws Exception {
        String url = "https://api.weibo.com/oauth2/authorize?client_id=" + //
                "109386371" + //
                "&response_type=code&redirect_uri=" + //
                "http://cduer.com/login" + //
                "&display=wap2.0";

        Document document = Jsoup.connect(url).get();

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

        map.put("userId", "limingwei@mail.com");
        map.put("passwd", "buxiaode");
        map.put("action", "submit");

        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://api.weibo.com/oauth2/authorize");
        post.addHeader("Referer", url);

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();

        for (Entry<String, String> arg : map.entrySet()) {
            formParams.add(new BasicNameValuePair(arg.getKey(), arg.getValue()));
        }

        HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");

        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        System.out.println("header location 在这里");
        System.out.println(response.getFirstHeader("Location").getValue());

        String code = "fe7af675c42e2ef17b7094f6030b9f62";

        String url3 = "https://api.weibo.com/oauth2/access_token?client_id=" + //
                "109386371" + //
                "&client_secret=" + //
                "debba9b103ece705dcd854fe7087b4b6" + //
                "&grant_type=" + //
                "authorization_code" + //
                "&redirect_uri=" + //
                "http://cduer.com/login" + //
                "&code=" + //
                "fe7af675c42e2ef17b7094f6030b9f62";

        // Document document = Jsoup.connect(url3).post();

        String response2 = "\"{" + //
                "\"access_token\":\"2.003rRhwB0_5y5Hae3969dd4b4sy55D\"," + //
                "\"remind_in\":\"157679999\"," + //
                "\"expires_in\":157679999," + //
                "\"uid\":\"1783477080\"" + //
                "}";

        String access_token = "2.003rRhwB0_5y5Hae3969dd4b4sy55D";

        // Timeline timeline = new Timeline();
        // timeline.setToken("2.003rRhwB0_5y5Hae3969dd4b4sy55D");
        // Status status = timeline.UpdateStatus("床前明月光,再来发一条-api发送");
        // System.out.println(status);
    }
}