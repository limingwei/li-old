package li.ioc;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import li.annotation.Inject;
import li.model.Bean;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;

/**
 * Ioc加载器,加载用注解方式配置的Bean
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.2 (2012-05-08)
 */
public class AnnotationIocLoader {

    private static final Log log = Log.init();

    private static final String CLASS_REGEX = "^.*\\.class$", JAR_REGEX = "^.*\\.jar$";;

    /**
     * 扫描 Source Floder 下的所有类文件, 将其中加了@Bean注解的类返回,然后被加入到IocContext
     */
    public List<Bean> getBeans() {
        List<String> fileList = this.getClasseFiles();
        List<li.model.Bean> beans = new ArrayList<li.model.Bean>();
        for (String classFileName : fileList) {
            try {
                Class<?> type = Reflect.getType(getClassName(classFileName));
                li.annotation.Bean beanAnnotation = type.getAnnotation(li.annotation.Bean.class);
                if (beanAnnotation != null) {
                    li.model.Bean iocBean = new li.model.Bean();// 一个新的Bean
                    iocBean.type = type;
                    iocBean.name = beanAnnotation.value();

                    List<Field> fields = Reflect.getFields(type);
                    for (Field field : fields) {
                        Inject inject = field.getAnnotation(Inject.class);
                        if (null != inject) {
                            li.model.Field attribute = new li.model.Field();// 一个新的Field
                            attribute.name = field.getName();
                            attribute.type = field.getType();
                            attribute.value = inject.value();
                            iocBean.fields.add(attribute);
                        }
                    }
                    beans.add(iocBean);

                    log.debug("ADD BEAN: @Bean ? ?", type.getName(), iocBean.name);
                }
            } catch (Throwable e) {
                // class not found 啥的，太多了，就先不打日志了
            }
        }
        return beans;
    }

    /**
     * 取/classes/之后的字符串(如果有/classes/的话),替换/为.,去掉.class
     */
    private static String getClassName(String classFileName) {
        int classesIndex = classFileName.indexOf(File.separator + "classes" + File.separator);
        return classFileName.substring(classesIndex > 0 ? classesIndex + 9 : 0, classFileName.length() - 6).replace(File.separatorChar, '.');
    }

    /**
     * 获取所有类文件,从class和jar
     */
    private List<String> getClasseFiles() {
        File rootFolder = Files.root();
        List<String> classFileList = Files.list(rootFolder, CLASS_REGEX, true, 1);
        log.info("Found ? class files, at ?", classFileList.size(), rootFolder);
        classFileList.addAll(this.getClassFilesInJar());
        return classFileList;
    }

    /**
     * 获取所有jar里面的类
     */
    private List<String> getClassFilesInJar() {
        try {
            List<String> classFileList = new ArrayList<String>();
            String jarLib = this.getLibFolder();
            List<String> jarFiles = Files.list(new File(jarLib), JAR_REGEX, true, 1);
            log.info("Found ? jar files , at ?", jarFiles.size(), jarLib);
            for (String jar : jarFiles) {
                JarFile jarFile = new JarFile(jar);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    if (!jarEntry.isDirectory()) {
                        String entryName = jarEntry.getName();
                        if (entryName.endsWith(".class")) {
                            classFileList.add(entryName);
                        }
                    }
                }
            }
            log.info("Found ? class files in jar, at ?", classFileList.size(), jarLib);
            return classFileList;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * 获得项目根路径
     */
    private String getLibFolder() {
        try {
            String path = this.getClass().getResource("/").toURI().getPath();
            String webRoot = new File(path).getParentFile().getParentFile().getCanonicalPath();
            List<String> libFolders = Files.list(new File(webRoot), "^.*\\" + File.separator + "WEB-INF\\" + File.separator + "lib$", true, 2);
            if (libFolders.size() == 1) {
                return libFolders.get(0);
            }
            log.warn("searching for /WEB-INF/lib , but not got one", libFolders);
            return webRoot + File.separator + "WEB-INF" + File.separator + "lib";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}