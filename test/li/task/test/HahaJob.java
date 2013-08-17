package li.task.test;

import java.util.Date;

import li.annotation.Bean;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Bean
public class HahaJob implements Runnable, Job {
    public void run() {
        System.out.println(Thread.currentThread() + "\t" + new Date() + "\t" + "AAA");
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        run();
    }
}