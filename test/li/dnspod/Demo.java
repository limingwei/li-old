package li.dnspod;

import li.http.Request;
import li.http.Response;

public class Demo {
    public static void main(String[] args) {
        Request request = new Request();
        request.setUrl("https://dnsapi.cn/Record.List");
        request.setField("login_email", "limingwei@mail.com");
        request.setField("login_password", "buxiaode");
        request.setField("format", "json");
        request.setField("lang", "en");
        request.setField("domain_id", "3155484");
        // request.setField("telephone", "13699466308");
        request.setUserAgent("DNSLI/0.1.0 (limingwei@mail.com)");
        Response response = request.execute();
        System.out.println(response.getBody());
    }
}