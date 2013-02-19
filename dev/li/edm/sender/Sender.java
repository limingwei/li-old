package li.edm.sender;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Sender {
    private Session session;
    private String username;

    public Sender(String host, String username, String password) {
        this(host, username, password, true);
    }

    public Sender(String host, final String username, final String password, Boolean debug) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", true);
        session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(debug);
        this.username = username;
    }

    public void send(Mail mail) {
        if (null == mail.getFrom() && null != this.username) {
            mail.setFrom(this.username);// 设置发件人为sender的用户名
        }
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getFrom()));// 发件人
            message.addRecipient(RecipientType.TO, new InternetAddress(mail.getTo()));// 收件人
            message.setSubject(mail.getSubject());// 邮件标题
            MimeMultipart multipart = new MimeMultipart();// 邮件内容
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(mail.getContent(), "text/html;charset=UTF-8");
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);
            Transport.send(message);// 发送邮件
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}