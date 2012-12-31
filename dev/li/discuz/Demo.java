package li.discuz;

import java.util.List;

import org.apache.http.cookie.Cookie;

public class Demo {
    static Discuz discuz = new Discuz("http://bbs.cduer.com/");

    static List<Cookie> cookies = null;

    // dinghuan 1406678816
    // 测试1 wode
    public static void main(String[] args) throws Exception {
        discuz.login("测试1", "wode");

        Integer fid = 143;
        String subject = "JAVA发布主题测试-视频";
        String message = "[flash=750,465]http://player.youku.com/player.php/sid/XNDk0OTU0OTgw/v.swf[/flash]";
        discuz.post(fid, subject, message);
    }
}