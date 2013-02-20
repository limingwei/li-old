package li.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Demo {
    static final String[] SUFFIX = { ".com", ".net", ".org", ".cn", ".mobi", ".name", ".cc", ".me", ".so", ".co", ".info", ".biz", ".hk", ".tv", ".tel", ".asia", ".travel", ".gov.cn", ".com.cn", ".net.cn", ".org.cn", ".ac.cn" };

    public static void main(String[] args) throws Exception {
        System.out.println(checkAll("cduer"));
    }

    private static String checkAll(String domain) throws Exception {
        String result = "";
        for (String suf : SUFFIX) {
            result += domain + suf + "\t" + check(domain + suf) + "\n";
            Thread.sleep(500);
        }
        return result;
    }

    private static Boolean check(String domain) throws Exception {
        String action = "http://www.xinnet.com/domain/check.do?method=check";
        HttpPost httpPost = new HttpPost(action);

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("domainName", domain));

        httpPost.setEntity(new UrlEncodedFormEntity(parameters));

        HttpClient httpClient = new DefaultHttpClient();

        String result = EntityUtils.toString(httpClient.execute(httpPost).getEntity());

        if (result.trim().toUpperCase().equals("TRUE")) {
            return true;
        } else if (result.trim().toUpperCase().equals("FALSE")) {
            return false;
        } else {
            System.err.println(result);
            return null;
        }
    }
}