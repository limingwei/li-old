package li.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * 文件工具类
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.5 (2012-05-08)
 */
public class Files {
    private static final Log log = Log.init();

    private static final String CONST_CONFIG_REGEX = "^.*(config|ioc|aop|const)\\.xml$";

    private static final String PROPERTIES_REGEX = "^.*\\.properties$";

    /**
     * 返回项目的classes目录
     */
    public static File root() {
        try {
            return new File(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * 根据文件名正则表达式搜索一个路径下的文件,返回文件路径的List
     * 
     * @param file 要搜索的目录
     * @param regex 要求文件路径要符合的正则表达式
     * @param increase 是否递进搜索
     * @param fileOrFloder 1 文件 2 文件夹
     * @return 文件绝对路径列表
     */
    public static List<String> list(File file, String regex, Boolean increase, Integer fileOrFloder) {
        List<String> list = new ArrayList<String>();
        if (((1 == fileOrFloder && file.isFile()) || (2 == fileOrFloder && file.isDirectory())) && Verify.regex(file.getPath(), regex)) {
            list.add(file.getPath());
        } else if (increase && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                list.addAll(list(f, regex, increase, fileOrFloder)); // 递归调用本方法
            }
        }
        return list;
    }

    /**
     * 根据文件路径path构建一个org.w3c.dom.Document
     * 
     * @param path XML文件路径,相对于classPath的相对路径
     */
    public static Document build(String path) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            return documentBuilderFactory.newDocumentBuilder().parse(path);
        } catch (Exception e) {
            log.error("? Files.build() path=?", e, path);
            return null;
        }
    }

    /**
     * 根据xpath表达式和returnType从document中读取值
     * 
     * @param document 被XPath解析的对象,Object类型,可以是Document,NodeList等
     * @param returnType XPathConstants枚举中的值,表示返回类型
     */
    public static Object xpath(Object document, String xpath, QName returnType) {
        try {
            return XPathFactory.newInstance().newXPath().compile(xpath).evaluate(document, returnType);
        } catch (Exception e) {
            log.error("? Files.xpath() document:? xpath:? returnType:?", e, document, xpath, returnType);
            return null;
        }
    }

    /**
     * 搜索并返回文件名包含 name的 Properties的并集,有缓存的
     */
    public static Properties load(String name) {
        Properties properties = (Properties) Log.get("~!@#PROPERTIES#" + name);// 从缓存中查找properties
        if (null == properties) {
            List<String> propertyFiles = (List<String>) Log.get("~!@#PROPERTIE_FILES");// 从缓存中查找propertyFiles
            if (null == propertyFiles) {
                File rootFolder = Files.root();
                propertyFiles = list(rootFolder, PROPERTIES_REGEX, true, 1);
                Log.put("~!@#PROPERTIE_FILES", propertyFiles); // 将 PROPERTIES文件列表缓存
                log.info("Found ? properties files , at ?", propertyFiles.size(), rootFolder);
            }

            properties = new Properties();
            for (Object filePath : propertyFiles) {
                if (Verify.contain((String) filePath, name)) {
                    try {
                        Properties prop = new Properties();
                        prop.load(new InputStreamReader(new BufferedInputStream(new FileInputStream((String) filePath)), "UTF-8"));
                        properties.putAll(prop);
                    } catch (Exception e) {
                        throw new RuntimeException(e + " ", e);
                    }
                }
            }
            Log.put("~!@#PROPERTIES#" + name, properties);// 将 properties 缓存
        }
        return properties;
    }

    public static Properties config() {
        Properties config = (Properties) Log.get("~!@#CONFIG");// 从缓存中查找properties
        if (null == config) {
            config = Files.load("config.properties");
            List<String> fileList = Files.list(Files.root(), CONST_CONFIG_REGEX, true, 1);// 搜索配置文件
            for (String filePath : fileList) {
                NodeList beanNodes = (NodeList) Files.xpath(Files.build(filePath), "//const", XPathConstants.NODESET);
                for (int length = (null == beanNodes ? -1 : beanNodes.getLength()), i = 0; i < length; i++) {
                    String name = Files.xpath(beanNodes.item(i), "@name", XPathConstants.STRING) + "";
                    String value = Files.xpath(beanNodes.item(i), "@value", XPathConstants.STRING) + "";
                    config.setProperty(name, value);
                }
            }
            Log.put("~!@#CONFIG", config);// 将 properties 缓存
        }
        return config;
    }
}