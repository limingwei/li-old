package li.mail;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * smtp.mailgun.org postmaster@limingwei.mailgun.org 6mitwv670n61
 * 
 * http://sendgrid.com
 */
public class JavaMail {
    public static void main(String[] args) {
        Sender sender = new Sender("smtp.mailgun.org", "postmaster@limingwei.mailgun.org", "6mitwv670n61");

        Mail mail = new Mail();
        mail.setSubject("这是一封测试邮件" + System.currentTimeMillis());
        mail.setContent(buildContent());
        mail.setTo("616319864@qq.com");
        sender.send(mail);
    }

    private static String buildContent() {
        Beetl beetl = new Beetl(new File("D:\\workspace\\li\\dev\\li\\mail\\mail_1.htm"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "黎明伟");
        map.put("date", "2012-10-22");
        return beetl.merge(map);
    }
}