package li.task;

import java.util.Date;

import li.annotation.Bean;

@Bean
public class HahaJob extends Job {
    @Override
    public void run() {
        System.out.println(Thread.currentThread() + "\t" + new Date() + "\t" + "AAA");
    }
}