package li.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;

/**
 * smtp.mailgun.org postmaster@limingwei.mailgun.org 6mitwv670n61
 * 
 * http://sendgrid.com
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        FM fm = new FM("tmpl.htm");
        Map<String, Object> map = new HashMap<String, Object>();

        List<Goods> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Goods goods = new Goods();
            goods.setTitle("商品1");
            goods.setUrl("#");
            goods.setImg("http://click.juliangli.com/static/team/2012/1128/13540838308493.jpg");
            list.add(goods);
        }

        map.put("goodsList", list);

        // Files.write(new File(System.getProperty("user.dir")+"\\dev\\li\\mail\\out.htm"), fm.merge(map));

        Sender sender = new Sender("smtp.mailgun.org", "postmaster@limingwei.mailgun.org", "6mitwv670n61");

        Mail mail = new Mail();
        mail.setSubject("本周精选货品推荐 Sense印象 精品女鞋专卖" + new Date());
        mail.setContent(fm.merge(map));
        mail.setFrom(MimeUtility.encodeText("Sense印象 精品女鞋专卖") + "<a@b.cn>");// 发件人

        mail.setTo("416133823@qq.com");
        sender.send(mail);
    }

    public static void main2(String[] args) throws Exception {
        Sender sender = new Sender("smtp.mailgun.org", "postmaster@limingwei.mailgun.org", "6mitwv670n61");

        Mail mail = new Mail();
        mail.setSubject("这是一封测试邮件" + System.currentTimeMillis());
        mail.setContent(buildContent());
        mail.setFrom(MimeUtility.encodeText("发件人啊这是") + "<limingwei@mail.com>");// 发件人

        mail.setTo("416133823@qq.com");
        sender.send(mail);
    }

    private static String buildContent() {
        Beetl beetl = new Beetl(new File(System.getProperty("user.dir") + "//dev//li//mail//mail_1.htm"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "黎明伟");
        map.put("date", "2012-10-22");
        return beetl.merge(map);
    }
}