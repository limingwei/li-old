package li.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.xpath.XPathConstants;

import li.ioc.Ioc;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.w3c.dom.NodeList;

/**
 * Quartz
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-03-19)
 */
public class Quartz {
    private static final Log log = Log.init();

    private static final String TASK_CONFIG_REGEX = "^.*(config|task|quartz)\\.xml$";

    /**
     * 防止重复启动的标记
     */
    private static Scheduler scheduler = null;

    /**
     * 初始化此类的时候启动Quartz,唯一的public方法
     */
    public Quartz() {
        try {
            log.debug("Starting Quartz ...");
            start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error when starting Quartz");
            throw new RuntimeException(e);
        }
    }

    /**
     * Quartz是否正在运行
     * 
     * @return
     */
    public static Boolean isOn() {
        try {
            return null != scheduler && scheduler.isStarted();
        } catch (SchedulerException e) {
            return false;
        }
    }

    /**
     * 启动Quartz,启动所有任务,synchronized方法
     */
    private synchronized static void start() throws Exception {
        if (null == scheduler) {// 只开始一次
            scheduler = getScheduler();
            Set<Entry<Class<? extends Runnable>, String>> jobs = getJobs().entrySet();
            for (Entry<Class<? extends Runnable>, String> entry : jobs) {
                String name = entry.getKey().getName();// 类名作为name,使用默认的GROUP
                JobDetail jobDetail = new JobDetailImpl(name, Scheduler.DEFAULT_GROUP, entry.getKey());
                CronTrigger cronTrigger = new CronTriggerImpl(name, Scheduler.DEFAULT_GROUP, entry.getValue());
                scheduler.scheduleJob(jobDetail, cronTrigger);
            }
            scheduler.start();
        } else {
            throw new RuntimeException("已经启动Quartz,不要多次启动");
        }
    }

    /**
     * 返回使用Ioc方式生成Job对象的Scheduler
     */
    private static Scheduler getScheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.setJobFactory(new SimpleJobFactory() {// 设置自定义的job生成工厂
                    public Job newJob(final TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
                        return new Job() {
                            public void execute(JobExecutionContext context) throws JobExecutionException {
                                Ioc.get(((JobDetailImpl) bundle.getJobDetail()).getJobClass2()).run();
                            }
                        };// 通过Ioc生成job实例
                    }
                });
        return scheduler;
    }

    /**
     * 扫描以config.xml或task.xml结尾的Quartz配置文件返回所有任务
     */
    private static Map<Class<? extends Runnable>, String> getJobs() {
        Map<Class<? extends Runnable>, String> jobs = new HashMap<Class<? extends Runnable>, String>();

        List<String> fileList = Files.list(Files.root(), TASK_CONFIG_REGEX, true);// 搜索以config.xml结尾的文件
        for (String filePath : fileList) {
            NodeList beanNodes = (NodeList) Files.xpath(Files.build(filePath), "//task", XPathConstants.NODESET);
            for (int length = (null == beanNodes ? -1 : beanNodes.getLength()), i = 0; i < length; i++) {
                String type = (String) Files.xpath(beanNodes.item(i), "@class", XPathConstants.STRING);
                String trigger = (String) Files.xpath(beanNodes.item(i), "@trigger", XPathConstants.STRING);

                jobs.put((Class<? extends Runnable>) Reflect.getType(type), trigger);
            }
        }
        return jobs;
    }
}

class JobDetailImpl extends org.quartz.impl.JobDetailImpl {
    private static final long serialVersionUID = 8564390067131366999L;

    private Class<? extends Runnable> jobClass2;

    public JobDetailImpl(String name, String group, Class<? extends Runnable> jobClass) {
        setName(name);
        setGroup(group);
        setJobClass(Job.class);
        this.jobClass2 = jobClass;
    }

    public Class<? extends Runnable> getJobClass2() {
        return this.jobClass2;
    }
}