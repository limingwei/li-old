package li.qq3g;

public class Demo {
    static QQ3G qq3g = new QQ3G();

    static String sid = "AcxG_g-NCyxDHM6GQaSjEJS2";// 1055515958

    public static void main(String[] args) throws Exception {
        // System.out.println(qq3g.login("1055515958", "buxiaode"));
        // System.out.println(qq3g.refresh(sid, "416133823"));
        System.out.println(qq3g.send(sid, "你好，这是一条测试消息，来自 黎明伟-3GQQ-JAVA", "416133823"));

        // System.out.println(qq3g.online(sid));
        // System.out.println(qq3g.groups(sid));

        // System.out.println(qq3g.group(sid, "我会的", "4", "4", "99"));
    }

    public static void sendMsgToQQs(String msg, String[] qqs) throws Exception {
        for (String qq : qqs) {
            // System.out.println(qq3g.send(sid, "你好,这是一条测试消息,收到请回答,来自 黎明伟-3GQQ-JAVA", qq));
            System.out.println(qq3g.send(sid, msg, qq));
            Thread.sleep(1000);
        }
    }

    public static void sendMsgsToQQ(String sid, String to, String[] msgs) throws Exception {
        for (String msg : msgs) {
            System.out.println(qq3g.send(sid, msg, to));
            Thread.sleep(1000);
        }
    }
}