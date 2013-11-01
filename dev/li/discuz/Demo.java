package li.discuz;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import li.util.ThreadUtil;

public class Demo {
    static Discuz discuz = new Discuz("http://bbs.cduer.com/");

    List<Account> accounts = Arrays.asList(new Account("测试1", "wode"));

    public static void main(String[] args) throws Exception {
        discuz.login2("测试1", "wode");

        for (int i = 0; i < 9; i++) {
            discuz.post2(143, i + " 测试发帖 " + UUID.randomUUID(), "测试发帖内容, 床前明月光  " + UUID.randomUUID());
            ThreadUtil.sleep(9000);
        }

        // Integer tid = 207468;
        // String message = "哎哟，还不错哦";
        // discuz.reply(tid, message);
    }
}