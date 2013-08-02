package li.task;

import li.ioc.Ioc;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * 在quartz的配置文件中加入org.quartz.scheduler.jobFactory.class=li.task.LiQuartzJobFactory
 */
public class LiJobFactory implements JobFactory {
    private static final SimpleJobFactory SIMPLE_JOB_FACTORY = new SimpleJobFactory();

    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
        Job job = Ioc.get(triggerFiredBundle.getJobDetail().getJobClass());
        if (null == job) {
            job = SIMPLE_JOB_FACTORY.newJob(triggerFiredBundle, scheduler);
        }
        return job;
    }
}