package li.net;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortScanner {
    static ExecutorService executorService = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {
        final String ip = "221.237.157.88";

        for (int i = 0; i <= 65535; i++) {
            final Integer port = i;
            executorService.submit(new Runnable() {
                public void run() {
                    if (check(ip, port)) {
                        System.out.println(ip + "\t" + port);
                    }
                }
            });
        }
    }

    public static Boolean check(String ip, Integer port) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            Socket socket = new Socket(address, port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}