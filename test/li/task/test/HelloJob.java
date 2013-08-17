package li.task.test;

import java.util.Date;

import li.annotation.Aop;
import li.annotation.Bean;
import li.annotation.Inject;
import li.aop.test._LogFilter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Bean
public class HelloJob implements Runnable, Job {

    @Inject("IOC注入的内容")
    private String IOC注入的信息;

    @Aop(_LogFilter.class)
    public void run() {
        System.out.println(Thread.currentThread() + "\t" + new Date() + "\t" + "BBB" + "\t" + IOC注入的信息);
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        run();
    }
}