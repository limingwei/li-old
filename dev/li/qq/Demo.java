package li.qq;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import li.util.Files;

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

public class Demo {
    public static void main(String[] args) throws Exception {
        String url = "http://pt.3g.qq.com/s?aid=nLogin3gqq&auto=1&s_it=1&g_f=286&sid=AbO79wm99_gRqY0wBafirEzk";
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        String action = "http://pt.3g.qq.com/psw3gqqLogin?r=75515933&vdata=6B38167FEBCAEF307A461CF3302D9A99";

        HttpPost post = new HttpPost(action);
        post.addHeader("Referer", url);

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();

        formParams.add(new BasicNameValuePair("qq", "416133823"));
        formParams.add(new BasicNameValuePair("pwd", "buxiaode"));
        formParams.add(new BasicNameValuePair("bid_code", "3GQQ"));
        formParams.add(new BasicNameValuePair("toQQchat", "true"));
        formParams.add(new BasicNameValuePair("login_url", "http://pt.3g.qq.com/s?aid=nLoginnew&q_from=3GQQ"));
        formParams.add(new BasicNameValuePair("q_from", ""));
        formParams.add(new BasicNameValuePair("modifySKey", "0"));
        formParams.add(new BasicNameValuePair("loginType", "1"));
        formParams.add(new BasicNameValuePair("aid", "nLoginHandle"));
        formParams.add(new BasicNameValuePair("i_p_w", "qq|pwd|"));

        HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");

        post.setEntity(entity);

        HttpResponse response2 = new DefaultHttpClient().execute(post);

        // Files.write(new File("C:\\Users\\li\\Desktop\\qq.htm"), EntityUtils.toString(response2.getEntity()));
        String content = EntityUtils.toString(response2.getEntity());

        String main = Jsoup.parse(content).select("a").attr("href");

        Files.write(new File("C:\\Users\\li\\Desktop\\qq.htm"), Jsoup.connect(main).get().toString());
    }
}