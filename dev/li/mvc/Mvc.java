package li.mvc;

import java.io.IOException;

import javax.servlet.ServletInputStream;

import li.annotation.At;
import li.annotation.Bean;

@Bean
public class Mvc {
    @At("upload.do")
    public static void upload() {
        try {
            ServletInputStream inputStream = Context.getRequest().getInputStream();
            byte[] tmp = new byte[1024];
            while (-1 != inputStream.readLine(tmp, 0, tmp.length)) {
                System.out.println("line = " + new String(tmp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}