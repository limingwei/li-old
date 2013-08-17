package li.task;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.xpath.XPathConstants;

import li.util.Files;
import li.util.Log;
import li.util.Reflect;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.w3c.dom.NodeList;

/**
 * Quartz
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2013-03-19)
 */
public class Quartz {
    private static final Log log = Log.init();

    private static final String TASK_CONFIG_REGEX = "^.*(config|task|quartz)\\.xml$";

    private static Scheduler scheduler = null;

    /**
     * 初始化此类的时候启动Quartz,唯一的public方法
     */
    public Quartz() {
        try {
            log.debug("Starting Quartz ...");
            start();
        } catch (Exception e) {
            log.error("Error when starting Quartz");
            throw new RuntimeException(e);
        }
    }

    /**
     * Quartz是否正在运行
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
            Set<Entry<Class<? extends Job>, String>> jobs = getJobs().entrySet();
            for (Entry<Class<? extends Job>, String> entry : jobs) {
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
        scheduler.setJobFactory(new LiJobFactory());
        return scheduler;
    }

    /**
     * 扫描以config.xml或task.xml或quartz.xml结尾的Quartz配置文件返回所有任务
     */
    private static Map<Class<? extends Job>, String> getJobs() {
        Map<Class<? extends Job>, String> jobs = new HashMap<Class<? extends Job>, String>();

        File rootFolder = Files.root();
        List<String> fileList = Files.list(rootFolder, TASK_CONFIG_REGEX, true);// 搜索配置文件
        log.info("Found ? quartz task xml config files, at ?", fileList.size(), rootFolder);

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
}