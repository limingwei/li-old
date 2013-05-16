package li.task;

import java.util.Date;

import li.annotation.Aop;
import li.annotation.Bean;
import li.annotation.Inject;
import li.aop._LogFilter;

@Bean
public class HelloJob extends Job {

    @Inject("IOC注入的内容")
    private String IOC注入的信息;

    @Aop(_LogFilter.class)
    public void run() {
        System.out.println(Thread.currentThread() + "\t" + new Date() + "\t" + "BBB" + "\t" + IOC注入的信息);
    }
}