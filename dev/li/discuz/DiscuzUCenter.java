package li.discuz;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fivestars.interfaces.bbs.client.Client;
import com.fivestars.interfaces.bbs.util.XMLHelper;

public class DiscuzUCenter {
    public static void main(String[] args) throws Exception {
        login("测试101", "wode");
    }

    public static void post() throws Exception {
        String url = "http://bbs.cduer.com/forum.php?mod=post&action=reply&fid=243&tid=197226&extra=page%3D1&replysubmit=yes&infloat=yes&handlekey=fastpost&inajax=1";

        Map<String, String> map = new HashMap<String, String>();

        map.put("message", "消息内容");
        map.put("posttime", "1350724284");
        map.put("formhash", "89b20919");
        map.put("subject", "");

        Document document = Jsoup.connect(url).data(map).post();

        System.out.println(document);
    }

    public static void login(String username, String password) {
        Client client = new Client();
        String result = client.uc_user_login(username, password);

        LinkedList<String> rs = XMLHelper.uc_unserialize(result);
        if (rs.size() > 0) {
            int $uid = Integer.parseInt(rs.get(0));
            String $username = rs.get(1);
            String $password = rs.get(2);
            String $email = rs.get(3);
            if ($uid > 0) {
                System.out.println("登录成功");
                System.out.println($username);
                System.out.println($password);
                System.out.println($email);

                String $ucsynlogin = client.uc_user_synlogin($uid);
                System.out.println("登录成功" + $ucsynlogin);

                // 本地登陆代码
                // TODO ... ....
            } else if ($uid == -1) {
                System.out.println("用户不存在,或者被删除");
            } else if ($uid == -2) {
                System.out.println("密码错");
            } else {
                System.out.println("未定义");
            }
        } else {
            System.out.println("Login failed");
            System.out.println(result);
        }
    }

    public static void register(String username, String password, String email) {
        Client uc = new Client();
        String $returns = uc.uc_user_register(username, password, email);
        int $uid = Integer.parseInt($returns);
        if ($uid <= 0) {
            if ($uid == -1) {
                System.out.print("用户名不合法");
            } else if ($uid == -2) {
                System.out.print("包含要允许注册的词语");
            } else if ($uid == -3) {
                System.out.print("用户名已经存在");
            } else if ($uid == -4) {
                System.out.print("Email 格式有误");
            } else if ($uid == -5) {
                System.out.print("Email 不允许注册");
            } else if ($uid == -6) {
                System.out.print("该 Email 已经被注册");
            } else {
                System.out.print("未定义");
            }
        } else {
            System.out.println("OK:" + $returns);
        }

    }
}