package li.mail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SMTPServer {
    public static void main(String args[]) throws IOException, UnknownHostException {
        String msgFile = "E:\\workspace\\li\\more\\li\\mail\\mail_1.htm";
        String from = "416133823@qq.com";
        String to = "cdusj@qq.com";
        String mailHost = "smtp.qq.com";
        SMTP mail = new SMTP(mailHost);
        if (mail != null) {
            if (mail.send(new FileReader(msgFile), from, to)) {
                System.err.println("Mail sent.");
            } else {
                System.err.println("Connect to SMTP server failed!");
            }
        }
        System.err.println("Done.");
    }

    static class SMTP {
        private final static int SMTP_PORT = 25;
        InetAddress mailHost;
        InetAddress localhost;
        BufferedReader in;
        PrintWriter out;

        public SMTP(String host) throws UnknownHostException {
            mailHost = InetAddress.getByName(host);
            localhost = InetAddress.getLocalHost();
            System.err.println("mailhost = " + mailHost);
            System.err.println("localhost= " + localhost);
        }

        public boolean send(FileReader msgFileReader, String from, String to) throws IOException {
            Socket smtpPipe;
            InputStream inn;
            OutputStream outt;
            BufferedReader msg;
            msg = new BufferedReader(msgFileReader);
            smtpPipe = new Socket(mailHost, SMTP_PORT);
            if (smtpPipe == null) {
                return false;
            }
            inn = smtpPipe.getInputStream();
            outt = smtpPipe.getOutputStream();
            in = new BufferedReader(new InputStreamReader(inn));
            out = new PrintWriter(new OutputStreamWriter(outt), true);
            if (inn == null || outt == null) {
                System.err.println("Failed to open streams to socket.");
                return false;
            }
            String initialID = in.readLine();
            System.err.println("initialID : " + initialID);
            System.err.println("HELO : " + localhost.getHostName());
            out.println("HELO " + localhost.getHostName());
            String welcome = in.readLine();
            System.err.println(welcome);
            System.err.println("MAIL From:<" + from + ">");
            out.println("MAIL From:<" + from + ">");
            String senderOK = in.readLine();
            System.err.println("senderOK : " + senderOK);
            System.err.println("RCPT TO:<" + to + ">");
            out.println("RCPT TO:<" + to + ">");
            String recipientOK = in.readLine();
            System.err.println(recipientOK);
            System.err.println("DATA");
            out.println("DATA");
            String line;
            while ((line = msg.readLine()) != null) {
                out.println(line);
            }
            System.err.println(".");
            out.println(".");
            String acceptedOK = in.readLine();
            System.err.println("acceptedOK : " + acceptedOK);
            System.err.println("QUIT");
            out.println("QUIT");
            return true;
        }
    }
}