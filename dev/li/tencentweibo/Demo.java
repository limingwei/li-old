package li.tencentweibo;

import java.io.IOException;

public class Demo {
    static TencentWeibo tencentWeibo = new TencentWeibo();

    public static void main(String[] args) throws Exception, IOException {
        String ip = "220.181.111.85";

        // tencentWeibo.authorize();
        String code = "0c4b76e016382c28f944fc455740a1d2";
        String openid = "C149FADBF72D266F998E974F8C1F3B6D";

        // tencentWeibo.access_token(code);
        String access_token = "89f4c51916faf1af37e3a689d2d81e6d";

        tencentWeibo.addWeibo(access_token, openid, ip, "床前明月光,测试微博内容");
    }
}