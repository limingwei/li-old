package li.edm.sender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.StringReader;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;

import li.dao.Page;
import li.edm.collector.record.Email;
import li.ioc.Ioc;
import li.util.Log;
import li.util.Verify;

import org.apache.commons.io.output.FileWriterWithEncoding;

/**
 * 663564毫秒 180封 2012-01-28 4s/封 15封/分 900封/小时 21600封/天
 */
public class Demo {
    private static final String UTF8 = "UTF-8";

    private static final String[] testMail = { "416133823@qq.com" };

    private static final String tempalte_path = "dev\\li\\edm\\sender\\edm_template_1.htm";

    private static final Sender sender = new Sender("smtp.mailgun.org", "postmaster@limingwei.mailgun.org", "6mitwv670n61");

    private static final Log log = Log.init();

    public static void main(String[] args) throws Exception {
        // preview();
        // testSend();
        startSendTask();
    }

    private static void testSend() throws Exception {
        for (String address : testMail) {
            sendMailTo(address);
        }
    }

    private static void preview() {
        Freemarker freemarker = new Freemarker(tempalte_path);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsList", data());
        map.put("mail", "preview@w.cn");

        write(new File("E:\\preview.htm"), freemarker.merge(map));
    }

    public static void write(File file, String content) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(content));
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriterWithEncoding(file, UTF8));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.close();
            bufferedReader.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception in li.util.Files.write(File, String)", e);
        }
    }

    private static void startSendTask() throws Exception {
        Email emailDao = Ioc.get(Email.class);
        List<Email> emails = emailDao.list(new Page(1, 150), "WHERE last_send_date IS NULL");

        for (Email email : emails) {
            String mailAddress = email.get(String.class, "address");
            try {
                sendMailTo(mailAddress);

                email.set("domain", getDomain(mailAddress));
                email.set("last_send_date", new Timestamp(System.currentTimeMillis()));
                emailDao.update(email);

                log.debug("sent ?", mailAddress);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("sent error ?", mailAddress);
            }
        }
    }

    public static void sendMailTo(String mailAddress) throws Exception {
        Freemarker freemarker = new Freemarker(tempalte_path);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsList", data());
        map.put("mail", mailAddress);

        Mail mail = new Mail();
        mail.setSubject("精选宝贝推荐 Sense印象 精品女鞋专卖 " + new Date(System.currentTimeMillis()));
        mail.setContent(freemarker.merge(map));
        mail.setFrom(MimeUtility.encodeText("Sense印象 精品女鞋专卖") + "<limingwei@mail.com>");// 发件人

        mail.setTo(mailAddress);
        sender.send(mail);
    }

    private static String getDomain(String email) {
        if (Verify.isEmpty(email)) {
            return "";
        }
        return email.substring(email.indexOf('@') + 1);
    }

    private static List<Deal> data() {
        List<Deal> list = new ArrayList<Deal>();

        Deal goods1 = new Deal();
        goods1.set("id", "18521351303");
        goods1.set("title", "豹纹金属纯色真兔毛皮毛一体平跟磨砂面浅口布面女单鞋低帮鞋");
        goods1.set("url", "http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.5.tGvb74&id=18521351303");
        goods1.set("img", "http://bbs.cduer.com/data/attachment/forum/201301/28/2201509hiibrmu978bembw.jpg");
        goods1.set("price", "52.00");
        list.add(goods1);

        Deal goods2 = new Deal();
        goods2.set("id", "18524463825");
        goods2.set("title", "春秋冬季新款皮带扣超高跟保暖马丁靴雪地靴坡跟厚底女鞋子");
        goods2.set("url", "http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.3.tGvb74&id=18524463825");
        goods2.set("img", "http://bbs.cduer.com/data/attachment/forum/201301/28/220153cfyzuyl6yf3nidyi.jpg");
        goods2.set("price", "118.00");
        list.add(goods2);

        Deal goods3 = new Deal();
        goods3.set("id", "16927542626");
        goods3.set("title", "冬季保暖纯色水钻内增高高跟防滑橡胶底磨砂短靴雪地靴女鞋子");
        goods3.set("url", "http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.8.tGvb74&id=16927542626");
        goods3.set("img", "http://bbs.cduer.com/data/attachment/forum/201301/28/220151cgb5tk4vvyttntyv.jpg.thumb.jpg");
        goods3.set("price", "78.00");
        list.add(goods3);

        Deal goods4 = new Deal();
        goods4.set("id", "21986128462");
        goods4.set("title", "新款实物拍摄前系带高跟粗跟中筒加厚雪地短靴可大小码女鞋子");
        goods4.set("url", "http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.2.tGvb74&id=21986128462");
        goods4.set("img", "http://bbs.cduer.com/data/attachment/forum/201301/28/22014855n3yqeyuoeuy333.jpg");
        goods4.set("price", "118.00");
        list.add(goods4);

        Deal goods5 = new Deal();
        goods5.set("id", "16935541235");
        goods5.set("title", "春秋冬季保暖皮带扣方跟低跟女式磨砂面短靴马丁靴");
        goods5.set("url", "http://item.taobao.com/item.htm?spm=a1z10.1.137712-558047901.6.tGvb74&id=16935541235");
        goods5.set("img", "http://bbs.cduer.com/data/attachment/forum/201301/28/220152bsobsffzc006spfp.jpg");
        goods5.set("price", "148.00");
        list.add(goods5);

        return list;
    }
}