package li.task;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.File;
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

import org.w3c.dom.NodeList;

/**
 * Cron4j
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2013-03-19)
 */
public class Cron4j {
    private static final Log log = Log.init();

    private static final String TASK_CONFIG_REGEX = "^.*(config|task|cron4j)\\.xml$";

    private static Scheduler scheduler = null;

    /**
     * 初始化此类的时候启动Cron4j,唯一的public方法
     */
    public Cron4j() {
        try {
            log.debug("Starting Cron4j ...");
            start();
        } catch (Exception e) {
            log.error("Error when starting Cron4j");
            throw new RuntimeException(e);
        }
    }

    /**
     * Cron4j是否正在运行
     */
    public static Boolean isOn() {
        try {
            return null != scheduler && scheduler.isStarted();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 启动Cron4j,启动所有任务,synchronized方法
     */
    private synchronized static void start() throws Exception {
        if (null == scheduler) {// 只开始一次
            scheduler = new Scheduler();
            Set<Entry<Class<? extends Runnable>, String>> jobs = getJobs().entrySet();
            for (final Entry<Class<? extends Runnable>, String> entry : jobs) {
                scheduler.schedule(entry.getValue(), Ioc.get(entry.getKey()));
            }
            scheduler.start();
        } else {
            throw new RuntimeException("已经启动Cron4j,不要多次启动");
        }
    }

    /**
     * 扫描以config.xml或task.xml或cron4j.xml结尾的Cron4j配置文件返回所有任务
     */
    private static Map<Class<? extends Runnable>, String> getJobs() {
        Map<Class<? extends Runnable>, String> jobs = new HashMap<Class<? extends Runnable>, String>();

        File rootFolder = Files.root();
        List<String> fileList = Files.list(rootFolder, TASK_CONFIG_REGEX, true);// 搜索配置文件
        log.info("Found ? cron4j task xml config files, at ?", fileList.size(), rootFolder);

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