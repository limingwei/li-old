package li.discuz;

import java.util.Arrays;
import java.util.List;

public class Demo {
    static Discuz discuz = new Discuz("http://bbs.cduer.com/");

    List<Account> accounts = Arrays.asList(new Account("测试1", "wode"));

    public static void main(String[] args) throws Exception {
        discuz.login("测试1", "wode");

        Integer tid = 201216;
        String message = "哎哟，还不错哦";
        discuz.reply(tid, message);
    }
}