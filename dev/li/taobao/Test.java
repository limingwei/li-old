package li.taobao;

import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
    public static void search(String keyWord, Integer page) throws Exception {
        String url = "http://s.m.taobao.com/search.htm?q="//
                + URLEncoder.encode(keyWord, "UTF-8")//
                + "#!page/-Z"//
                + page;

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        HttpResponse response = client.execute(get);

        Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()));

        Elements elements = document.select("div.detail");
        for (Element element : elements) {
            if (!element.select("td.pic").isEmpty()) {
                System.out.println(element.select("a").text());
                Elements eles = element.select("strong.red");
                System.out.println(eles.first().text());
                System.out.println(eles.last().text());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        search("女鞋", 3);
    }
}