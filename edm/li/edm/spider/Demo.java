package li.edm.spider;

import java.util.ArrayList;
import java.util.List;

import li.edm.sender.Deal;
import li.ioc.Ioc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Demo {
    static String[] urls = { "http://senseyinxiang.taobao.com/category.htm?pageNo=1",//
            "http://senseyinxiang.taobao.com/category.htm?pageNo=2", //
            "http://senseyinxiang.taobao.com/category.htm?pageNo=3" };

    public static void main(String[] args) throws Exception {
        Deal dealDao = Ioc.get(Deal.class);

        List<Deal> deals = new ArrayList<>();
        for (String url : urls) {
            Document document = Jsoup.connect(url).timeout(20 * 1000).get();
            Elements dealElements = document.select("dl.item");
            for (Element dealElement : dealElements) {
                Deal deal = new Deal();
                deal.set("title", dealElement.select("a.item-name").text());
                deal.set("url", dealElement.select("dt.photo").select("a").attr("href"));
                deal.set("img", dealElement.select("dt.photo").select("img").attr("src"));
                deal.set("price", dealElement.select("div.cprice-area").select("span.c-price").text());
                deals.add(deal);
            }
        }

        for (Deal deal : deals) {
            dealDao.save(deal);
        }
    }
}