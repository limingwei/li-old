package li.feixin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import li.util.Files;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

public class Feixin {
    public void login() throws Exception {
        String url = "http://f.10086.cn/im/";

        Files.write(new File("C:/Users/li/Desktop/feixin.htm"), Jsoup.connect(url).get().toString());

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://f.10086.cn/im/login/inputpass.action");

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("pass", "211isme"));// password
        formParams.add(new BasicNameValuePair("loginstatus", "1"));// 1在线 2忙碌 3离开 4隐身
        formParams.add(new BasicNameValuePair("m", "13699466308"));
        formParams.add(new BasicNameValuePair("captchaCode", ""));

        HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        Files.write(new File("C:/Users/li/Desktop/feixin.htm"), EntityUtils.toString(response.getEntity()));
    }
}