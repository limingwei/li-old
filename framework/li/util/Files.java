package li.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * 文件工具类
 * 
 * @author li (limw@w.cn)
 * @version 0.1.5 (2012-05-08)
 */
public class Files {
    private static final Log log = Log.init();

    private static final String PROPERTIES_REGEX = "^.*\\.properties$";

    /**
     * 返回项目的classes目录
     */
    public static File root() {
        return new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }

    /**
     * 根据文件名正则表达式搜索一个路径下的文件,返回文件路径的List
     * 
     * @param file 要搜索的目录
     * @param regex 要求文件路径要符合的正则表达式
     * @param increase 是否递进搜索
     * @return 文件绝对路径列表
     */
    public static List<String> list(File file, String regex, Boolean increase) {
        List<String> list = new ArrayList<String>();
        if (file.isFile() && Verify.regex(file.getPath(), regex)) {
            list.add(file.getPath());
        } else if (increase && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                list.addAll(list(f, regex, increase)); // 递归调用本方法
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
        Properties properties = (Properties) Log.get("PROPERTIE#" + name);// 从缓存中查找properties
        if (null == properties) {
            List propertyFiles = (List) Log.get("PROPERTIE_FILES");// 从缓存中查找propertyFiles
            if (null == propertyFiles) {
                File rootFolder = Files.root();
                propertyFiles = list(rootFolder, PROPERTIES_REGEX, true);
                Log.put("PROPERTIE_FILES", propertyFiles); // 将 PROPERTIES文件列表缓存
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
                        throw new RuntimeException("Exception in li.util.Files.load(String)", e);
                    }
                }
            }
            Log.put("PROPERTIE#" + name, properties);// 将 properties 缓存
        }
        return properties;
    }
}