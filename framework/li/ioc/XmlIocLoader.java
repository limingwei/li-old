package li.ioc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import li.model.Bean;
import li.model.Field;
import li.util.Files;
import li.util.Log;
import li.util.Reflect;

import org.w3c.dom.NodeList;

/**
 * Ioc加载器,加载用Xml文件配置的Bean
 * 
 * @author li (limw@w.cn)
 * @version 0.1.4 (2012-05-08)
 */
public class XmlIocLoader {
    private static final Log log = Log.init();

    private static final String IOC_CONFIG_REGEX = "^.*(config|ioc)\\.xml$";

    /**
     * 解析SourceFloder下搜索到的文件名以config.xml结尾的文件,将其中配置的Bean返回
     */
    public List<Bean> getBeans() {
        File rootFolder = Files.root();
        List<String> fileList = Files.list(rootFolder, IOC_CONFIG_REGEX, true);// 搜索以config.xml结尾的文件
        log.info("Found ? Xml config files at ?", fileList.size(), rootFolder);

        List<Bean> beans = new ArrayList<Bean>();
        for (String filePath : fileList) {
            NodeList beanNodes = (NodeList) Files.xpath(Files.build(filePath), "//bean", XPathConstants.NODESET);
            for (int length = (null == beanNodes ? -1 : beanNodes.getLength()), i = 0; i < length; i++) {
                Bean iocBean = new Bean();// 一个新的Bean
                iocBean.name = Files.xpath(beanNodes.item(i), "@name", XPathConstants.STRING).toString();
                String type = Files.xpath(beanNodes.item(i), "@class", XPathConstants.STRING).toString();
                try {
                    iocBean.type = Class.forName(type);
                } catch (ClassNotFoundException e) {// 配置文件中把类名写错了
                    throw new RuntimeException("Class " + type + " not found , which is configured in " + filePath, e);
                }
                NodeList propertyNodes = (NodeList) Files.xpath(beanNodes.item(i), "property", XPathConstants.NODESET);
                for (int len = (null == propertyNodes ? -1 : propertyNodes.getLength()), m = 0; m < len; m++) {
                    Field field = new Field();// 一个新的Field
                    field.name = (String) Files.xpath(propertyNodes.item(m), "@name", XPathConstants.STRING);
                    field.type = Reflect.fieldType(iocBean.type, field.name);
                    field.value = (String) Files.xpath(propertyNodes.item(m), "@value", XPathConstants.STRING);
                    iocBean.fields.add(field);
                }
                beans.add(iocBean);

                log.debug("ADD BEAN: Xml ? ?", iocBean.type.getName(), iocBean.name);
            }
        }
        return beans;
    }
}