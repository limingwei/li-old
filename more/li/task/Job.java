package li.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务的抽象基类
 * 
 * @author li
 */
public abstract class Job implements org.quartz.Job, Runnable {
    /**
     * 重写这个方法或者run()方法添加任务内容,使用Quartz时必须重写两个方法中的一个
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        run();
    }

    /**
     * 重写这个方法或者execute()方法添加任务内容,使用Cron4j时候必须重写此方法
     */
    public void run() {
        try {
            execute(null);
        } catch (JobExecutionException e) {
            e.printStackTrace();
        }
    }
}