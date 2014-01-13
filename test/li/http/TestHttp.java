package li.http;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import li.util.Convert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class TestHttp {
    public static void main(String[] args) {
        Integer times = 1000;
        Long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            Request request = new Request();
            request.setUrl("http://localhost:8080/li_osc/performance_test_action.htm");
            request.execute();
        }
        System.out.println("##################  运行 " + times + " 次 耗时 " + (System.currentTimeMillis() - start) + " 毫秒");
    }

    @Test
    public void test() {
        System.out.println(new Request().setUrl("http://localhost:8080/site_analysis/view.htm?_token=" + "1106y5npjy6gb" + "&a=1&b=2&c=3").execute().getBody());
        System.out.println(new Request().setUrl("http://localhost:8080/site_analysis/view.htm?_token=" + "1106y5npjy6gb" + "&a=1&b=2&c=3").setMethod(Request.POST).execute().getBody());
    }

    @Test
    public void testCookie() {
        Request request = new Request();
        request.setUrl("g.cn");
        Response response = request.execute();
        System.out.println("body = " + response.getCookies());
    }

    @Test
    public void testHttpNotStringArgs() {
        Request request = new Request();
        request.setUrl("g.cn");
        request.setParameters(Convert.toMap(1, 2, 3, 4));
        Response response = request.execute();
        System.out.println(response.getBody());
    }

    /**
     * @Before
     */
    public void before() {
        System.setProperty("http.proxyHost", "208.110.94.187");
        System.setProperty("http.proxyPort", "8089");
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("username", "password".toCharArray());
            }
        });
        System.setProperty("sun.net.client.defaultConnectTimeout", "50000");
        System.setProperty("sun.net.client.defaultReadTimeout", "50000");
    }

    @Test
    public void testUrlPreFix() {
        Request request = new Request();
        request.setUrl("t.cn");
        Response response = request.execute();
        System.out.println(response.getBody());
    }

    @Test
    public void localSiteUpload() throws MalformedURLException {
        Request request = new Request();
        request.setUrl("http://localhost:8080/webqq/upload.do");
        request.setField("file_input_name_1", new URL("http://www.qqtouxiang888.com/uploads/allimg/100917/1J25HD0-15.gif"));
        request.setField("file_input_name_2", new URL("http://img2.duitang.com/uploads/item/201210/02/20121002233322_45svm.gif"));
        request.setField("text_input_name_1", "床前明月光");
        request.setField("text_input_name_2", "疑是地上霜");
        request.setField("text_input_name_3", "举头望明月");
        request.setField("text_input_name_4", "低头思故乡");
        request.execute();
    }

    /**
     * 代理访问测试
     */
    @Test
    public void testProxy() {
        List<Proxy> proxies = Arrays.asList(null, //
                Proxy.NO_PROXY, //
                new Proxy(Type.HTTP, new InetSocketAddress("208.110.94.187", 8089)),//
                new Proxy(Type.HTTP, new InetSocketAddress("113.28.244.195", 3128)),//
                new Proxy(Type.HTTP, new InetSocketAddress("190.122.185.195", 8080)),//
                new Proxy(Type.HTTP, new InetSocketAddress("118.97.243.2", 3128)),//
                new Proxy(Type.HTTP, new InetSocketAddress("202.171.253.111", 80)),//

                new Proxy(Type.HTTP, new InetSocketAddress("42.96.141.69", 8081)),//
                new Proxy(Type.HTTP, new InetSocketAddress("42.96.169.181", 81)),//
                new Proxy(Type.HTTP, new InetSocketAddress("58.53.192.218", 8123)),//
                new Proxy(Type.HTTP, new InetSocketAddress("58.210.247.18", 1337)),//
                new Proxy(Type.HTTP, new InetSocketAddress(" 58.213.157.68", 1080))//
                );

        for (Proxy proxy : proxies) {
            Request request = new Request();
            request.setUrl("http://www.baidu.com/s");
            request.setParameter("wd", "ip");
            request.setProxy(proxy);
            try {
                Response response = request.execute();
                Document document = Jsoup.parse(response.getBody());
                System.out.println("SUCCESS " + document.select("p.op_ip_detail").text());
            } catch (Exception e) {
                System.out.println("ERROR " + proxy + "\t" + e);
            }
        }
    }

    /**
     * 腾讯微博接口调用
     */
    @Test
    public void tencentWeibo() throws Exception {
        Request request = new Request();
        request.setUrl("https://open.t.qq.com/api/t/add_pic");
        request.setField("format", "json");
        request.setField("content", "你好 二 " + UUID.randomUUID());
        request.setField("clientip", "182.150.167.194");
        request.setField("longitude", "104.0683043");
        request.setField("latitude", "30.8176374");
        request.setField("pic", new URL("http://img2.duitang.com/uploads/item/201210/02/20121002233322_45svm.gif"));

        request.setField("oauth_consumer_key", "801329627");
        request.setField("access_token", "5a61e495941e2f17088df3b9374aea1a");
        request.setField("openid", "161EAD0091E60F2A82A7FF7A97BDA1E5");
        request.setField("oauth_version", "2.a");
        request.setField("scope", "all");

        Response response = request.execute();

        System.out.println(response.getStatus());
        System.out.println(response.getBody());
    }

    /**
     * 新浪微博接口调用
     */
    @Test
    public void sinaWeibo() throws Exception {
        Request request = new Request();
        request.setUrl("https://api.weibo.com/2/statuses/upload.json");

        request.setField("access_token", "2.003rRhwBbj3kJBdea73825c41QOvJD");
        request.setField("status", "你好 二 " + UUID.randomUUID());
        request.setField("pic", new URL("http://img2.duitang.com/uploads/item/201210/02/20121002233322_45svm.gif"));
        request.setField("lat", "30.8176374");
        request.setField("long", "104.0683043");

        Response response = request.execute();

        System.out.println(response.getStatus());
        System.out.println(response.getBody());
    }

    /**
     * 人人网开放平台
     */
    @Test
    public void renren() {
        Request request = new Request();
        request.setUrl("qq.com");
        Response response = request.execute();
        System.out.println(response.getStatus());
        System.out.println(response.getBody());
    }

    /**
     * nutz.cn 短地址生成
     */
    @Test
    public void nutzCreateUrl() {
        for (int i = 0; i < 9999; i++) {
            Request request = new Request();
            request.setUrl("http://nutz.cn/api/create/url");
            request.setHeader("hello", "you");
            request.setContentType(Request.FORM_URLENCODED);
            request.setField("data", "http://nutz.cn/hello");
            Response response = request.execute();
            System.out.println(response.getBody());
        }
    }

    /**
     * nutz.cn 短地址生成
     */
    @Test
    public void nutzCreateTxt() {
        for (int i = 0; i < 9999; i++) {
            Request request = new Request() {
                protected byte[] getData() throws Exception {
                    return "床前明月光".getBytes();
                }
            };
            request.setUrl("http://nutz.cn/api/create/txt");
            request.setMethod(Request.POST);
            request.setHeader("hello", "you");
            request.setContentType(Request.FORM_URLENCODED);
            Response response = request.execute();
            System.out.println(response.getBody());
        }
    }

    /**
     * nutz.cn 短地址生成
     */
    @Test
    public void nutzCreateFile() {
        for (int i = 0; i < 9999; i++) {
            Request request = new Request();
            request.setUrl("http://nutz.cn/api/create/url");
            request.setHeader("hello", "you");
            request.setContentType(Request.FORM_URLENCODED);
            request.setField("data", "http://nutz.cn/hello");
            Response response = request.execute();
            System.out.println(response.getBody());
        }
    }
}