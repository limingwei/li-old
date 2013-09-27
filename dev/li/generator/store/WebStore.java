package li.generator.store;

import java.util.List;

import li.generator.resource.Resource;
import li.http.Request;

public class WebStore extends Store {
    public List<Resource> getResources() {
        return null;
    }

    public static void main(String[] args) {
        System.err.println(new Request()//
                .setReferer("https://github.com/limingwei/li")//
                .setUserAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.1 (KHTML, like Gecko) Maxthon/4.1.3.1200 Chrome/26.0.1410.43 Safari/537.1")//
                .setUrl("http://git.oschina.net")//
                .execute().getBody());
    }
}