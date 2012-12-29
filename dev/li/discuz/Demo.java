package li.discuz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Demo {
    private static final String UTF8 = "UTF-8";
    private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();

    public static void main(String[] args) {
        String referer = "http://bbs.cduer.com/member.php?mod=register";
        String action = "http://bbs.cduer.com/member.php?mod=logging&action=login&loginsubmit=yes&handlekey=login&loginhash=LpEMK&inajax=1";

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("username", "dinghuan"));
        formParams.add(new BasicNameValuePair("password", "1406678816"));
        formParams.add(new BasicNameValuePair("loginsubmit", "true"));
        formParams.add(new BasicNameValuePair("answer", ""));
        formParams.add(new BasicNameValuePair("questionid", "0"));
        formParams.add(new BasicNameValuePair("referer", "http%3A%2F%2Fbbs.cduer.com%2Fmember.php%3Fmod%3Dregister"));
        formParams.add(new BasicNameValuePair("formhash", "2b903052"));

        HttpPost post = new HttpPost(action);
        post.setHeader("Referer", referer);
        post.setEntity(urlEncodedFormEntity(formParams, UTF8));

        HttpResponse response = execute(HTTP_CLIENT, post);

        System.out.println(content(response.getEntity()));
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