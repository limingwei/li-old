package li.weibo;

import weibo4j.Timeline;
import weibo4j.model.Status;

public class Demo {
    public static void main(String[] args) throws Exception {
        String url = "https://api.weibo.com/oauth2/authorize?client_id=" + //
                "109386371" + //
                "&response_type=code&redirect_uri=" + //
                "http://cduer.com/login";

        String code = "fe7af675c42e2ef17b7094f6030b9f62";

        String url3 = "https://api.weibo.com/oauth2/access_token?client_id=" + //
                "109386371" + //
                "&client_secret=" + //
                "debba9b103ece705dcd854fe7087b4b6" + //
                "&grant_type=" + //
                "authorization_code" + //
                "&redirect_uri=" + //
                "http://cduer.com/login" + //
                "&code=" + //
                "fe7af675c42e2ef17b7094f6030b9f62";

        // Document document = Jsoup.connect(url3).post();

        String response = "\"{" + //
                "\"access_token\":\"2.003rRhwB0_5y5Hae3969dd4b4sy55D\"," + //
                "\"remind_in\":\"157679999\"," + //
                "\"expires_in\":157679999," + //
                "\"uid\":\"1783477080\"" + //
                "}";

        String access_token = "2.003rRhwB0_5y5Hae3969dd4b4sy55D";

        Timeline timeline = new Timeline();
        timeline.setToken("2.003rRhwB0_5y5Hae3969dd4b4sy55D");
        Status status = timeline.UpdateStatus("床前明月光,再来发一条-api发送");
        System.out.println(status);

    }
}