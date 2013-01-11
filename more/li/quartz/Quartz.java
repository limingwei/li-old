package li.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.xpath.XPathConstants;

import li.ioc.Ioc;
import li.util.Files;
import li.util.Reflect;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.w3c.dom.NodeList;

public class Quartz {
    private static final String QUARTZ_CONFIG_REGEX = "^.*(config)|(task)\\.xml$";

    /**
     * 初始化此类的时候启动Quartz,唯一的public方法
     */
    public Quartz() {
        start();
    }

    /**
     * 防止重复启动的标记
     */
    private static boolean started = false;

    /**
     * 启动Quartz,启动所有任务,synchronized方法
     */
    private synchronized static void start() {
        if (!started) {
            try {
                Scheduler scheduler = getScheduler();
                Set<Entry<Class<? extends Job>, String>> jobs = getJobs().entrySet();
                for (Entry<Class<? extends Job>, String> entry : jobs) {
                    String name = entry.getKey().getName();// 类名作为name,使用默认的GROUP
                    JobDetail jobDetail = new JobDetailImpl(name, Scheduler.DEFAULT_GROUP, entry.getKey());
                    CronTrigger cronTrigger = new CronTriggerImpl(name, Scheduler.DEFAULT_GROUP, entry.getValue());
                    scheduler.scheduleJob(jobDetail, cronTrigger);
                }
                scheduler.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        started = true;
    }

    /**
     * 扫描以config.xml结尾的Quartz配置文件返回所有任务
     */
    private static Map<Class<? extends Job>, String> getJobs() {
        Map<Class<? extends Job>, String> jobs = new HashMap<Class<? extends Job>, String>();

        List<String> fileList = Files.list(Files.root(), QUARTZ_CONFIG_REGEX, true);// 搜索以config.xml结尾的文件
        for (String filePath : fileList) {
            NodeList beanNodes = (NodeList) Files.xpath(Files.build(filePath), "//task", XPathConstants.NODESET);
            for (int length = (null == beanNodes ? -1 : beanNodes.getLength()), i = 0; i < length; i++) {
                String type = (String) Files.xpath(beanNodes.item(i), "@class", XPathConstants.STRING);
                String trigger = (String) Files.xpath(beanNodes.item(i), "@trigger", XPathConstants.STRING);

                jobs.put((Class<? extends Job>) Reflect.getType(type), trigger);
            }
        }
        return jobs;
    }

    /**
     * 返回使用Ioc方式生成Job对象的Scheduler
     */
    private static Scheduler getScheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.setJobFactory(new SimpleJobFactory() {// 设置自定义的job生成工厂
                    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
                        return Ioc.get(bundle.getJobDetail().getJobClass());// 通过Io生成job
                    }
                });
        return scheduler;
    }
}