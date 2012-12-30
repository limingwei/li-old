package li.discuz;

import java.util.List;

import org.apache.http.cookie.Cookie;

public class Demo {
    static Discuz discuz = new Discuz();

    static List<Cookie> cookies = null;

    // dinghuan 1406678816
    public static void main(String[] args) {
        cookies = discuz.login("dinghuan", "1406678816");

        discuz.index(cookies);
    }
}