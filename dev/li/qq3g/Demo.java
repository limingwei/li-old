package li.qq3g;

public class Demo {
    static QQ3G qq3g = new QQ3G();

    static String sid = "AY6ZZi03_f_OSUNccbwubWiC";

    public static void main(String[] args) throws Exception {
        // System.out.println(qq3g.login("1055515958", "buxiaode"));
        // System.out.println(qq3g.refresh(sid, "416133823"));
        // System.out.println(qq3g.send(sid, "你好，这是一条测试消息，来自 黎明伟-3GQQ-JAVA", "416133823"));

        send("416133823", "616319864", "154520174", "767702147");
    }

    public static void send(String... qqs) throws Exception {
        for (String qq : qqs) {
            System.out.println(qq3g.send(sid, "你好,这是一条测试消息,收到请回答,来自 黎明伟-3GQQ-JAVA", qq));
            Thread.sleep(1000);
        }
    }
}