package li.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ping {
    static ExecutorService executorService = Executors.newFixedThreadPool(25);

    public static void main(String[] args) throws Exception {
        for (int a = 0; a < 256; a++) {
            for (int b = 0; b < 256; b++) {
                final String ip = "192.168." + a + "." + b;
                executorService.submit(new Runnable() {
                    public void run() {
                        if (check(ip)) {
                            System.out.println(ip);
                        }
                    }
                });
            }
        }
    }

    public static Boolean check(String ip) {
        String result = ping(ip);
        return result.contains("的回复");
    }

    public static String ping(String ip) {
        try {
            String cmd = "ping " + ip;
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            StringWriter stringWriter = new StringWriter();
            String buf;
            while ((buf = bufferedReader.readLine()) != null) {
                stringWriter.write(buf + "\n");
            }
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}