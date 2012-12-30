package li.discuz;

import java.util.List;

import org.apache.http.cookie.Cookie;

public class Demo {
    static Discuz discuz = new Discuz();

    static List<Cookie> cookies = null;

    // dinghuan 1406678816
    // 测试101 wode
    public static void main(String[] args) throws Exception {
        discuz.login("dinghuan", "1406678816");

        Thread.sleep(1000);
        Integer fid = 143;
        String subject = "JAVA发布主题测试";
        String message = "测试主题内容";
        discuz.post(fid, subject, message);
    }
}