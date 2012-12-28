package li.tencentweibo;

import java.util.Date;

public class Demo implements Config {
    static TencentWeibo tencentWeibo = new TencentWeibo();
    static String open_id = "C149FADBF72D266F998E974F8C1F3B6D";
    static String code = "fa558e94af565908863772d6565741eb";
    static String access_token = "e507a9371eff530ff9e8d1db16947df8";

    public static void main(String[] args) throws Exception {
        System.out.println(tencentWeibo.addWeibo(access_token, open_id, "我爱你,塞北的雪 @fengkuangfeifei 来自微博机器人" + new Date()));
    }
}