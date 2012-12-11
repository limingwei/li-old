package li.mail;

import org.apache.commons.mail.SimpleEmail;

public class CommonsMail {
    private static final String[] MAILS = { "cdusj@qq.com", "154520174@qq.com", "1406678816@qq.com", "767702147@qq.com", "136122073@qq.com", "965245282@qq.com", "1694352061@qq.com", "1029207678@qq.com", "1019265060@qq.com", "821721345@qq.com", "847096360@qq.com", "863314346@qq.com", "55942119@qq.com" };

    public static void main(String[] args) throws Exception {
        SimpleEmail email = new SimpleEmail(); // 构造一个mail对象
        email.setHostName("smtp.qq.com");
        email.setFrom("416133823@qq.com");// 设置发送人
        email.setAuthentication("416133823@qq.com", "buxiaode");
        email.setSubject("这是一封测试邮件");// 设置主题
        email.setContent("测试内容", "text/plain;charset=GBK");// 设置内容
        email.addTo(MAILS[0]);
        email.send();// 发送
        System.out.println("done");
    }
}