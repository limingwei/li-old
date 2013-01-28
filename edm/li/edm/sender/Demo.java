package li.edm.sender;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;

import li.dao.Page;
import li.edm.collector.record.Email;
import li.ioc.Ioc;
import li.util.Verify;

/**
 * 663564毫秒 180封 2012-01-28 4s/封 15封/分 900封/小时 21600封/天
 */
public class Demo {

    private static Date date = new Date();

    public static void main(String[] args) throws Exception {
        Email emailDao = Ioc.get(Email.class);
        List<Email> emails = emailDao.list(new Page(1, 180), "WHERE last_send_date IS NULL");

        for (Email email : emails) {
            sendMailTo(email.get(String.class, "address"));
            email.set("domain", getDomain(email.get(String.class, "address")));
            email.set("last_send_date", date);
            emailDao.update(email);
        }
    }

    private static String getDomain(String email) {
        if (Verify.isEmpty(email)) {
            return "";
        }
        return email.substring(email.indexOf('@') + 1);
    }

    public static void sendMailTo(String mailAddress) throws Exception {
        Freemarker freemarker = new Freemarker("edm\\li\\edm\\sender\\edm_tamplate_1.htm");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsList", data());

        Sender sender = new Sender("smtp.mailgun.org", "postmaster@limingwei.mailgun.org", "6mitwv670n61");

        Mail mail = new Mail();
        mail.setSubject("精选宝贝推荐 Sense印象 精品女鞋专卖" + new Date());
        mail.setContent(freemarker.merge(map));
        mail.setFrom(MimeUtility.encodeText("Sense印象 精品女鞋专卖") + "<limingwei@mail.com>");// 发件人

        mail.setTo(mailAddress);
        sender.send(mail);
    }

    private static List<Goods> data() {
        List<Goods> list = new ArrayList<Goods>();

        Goods goods1 = new Goods();
        goods1.setTitle("豹纹金属纯色真兔毛皮毛一体平跟磨砂面浅口布面女单鞋低帮鞋");
        goods1.setUrl("http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.5.tGvb74&id=18521351303");
        goods1.setImg("http://bbs.cduer.com/data/attachment/forum/201301/28/2201509hiibrmu978bembw.jpg");
        goods1.setPrice("52.00");
        list.add(goods1);

        Goods goods2 = new Goods();
        goods2.setTitle("春秋冬季新款包邮皮带扣超高跟保暖马丁靴雪地靴坡跟厚底女鞋子");
        goods2.setUrl("http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.3.tGvb74&id=18524463825");
        goods2.setImg("http://bbs.cduer.com/data/attachment/forum/201301/28/220153cfyzuyl6yf3nidyi.jpg");
        goods2.setPrice("118.00");
        list.add(goods2);

        Goods goods3 = new Goods();
        goods3.setTitle("包邮冬季保暖纯色水钻内增高高跟防滑橡胶底磨砂短靴雪地靴女鞋子");
        goods3.setUrl("http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.8.tGvb74&id=16927542626");
        goods3.setImg("http://bbs.cduer.com/data/attachment/forum/201301/28/220151cgb5tk4vvyttntyv.jpg.thumb.jpg");
        goods3.setPrice("78.00");
        list.add(goods3);

        Goods goods4 = new Goods();
        goods4.setTitle("新款实物拍摄包邮前系带高跟粗跟中筒加厚雪地短靴可大小码女鞋子");
        goods4.setUrl("http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.2.tGvb74&id=21986128462");
        goods4.setImg("http://bbs.cduer.com/data/attachment/forum/201301/28/22014855n3yqeyuoeuy333.jpg");
        goods4.setPrice("118.00");
        list.add(goods4);

        Goods goods5 = new Goods();
        goods5.setTitle("春秋冬季保暖皮带扣方跟低跟女式磨砂面短靴马丁靴");
        goods5.setUrl("http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.6.tGvb74&id=16935541235");
        goods5.setImg("http://bbs.cduer.com/data/attachment/forum/201301/28/220152bsobsffzc006spfp.jpg");
        goods5.setPrice("148.00");
        list.add(goods5);

        return list;
    }
}