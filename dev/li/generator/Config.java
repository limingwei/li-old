package li.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import li.util.Files;
import li.util.Log;

/**
 * 配置基类
 * 
 * @author 明伟
 */
public class Config {
    protected static final Log log = Log.init();

    private Map config;

    public String getConfigPropertiesFileName() {
        return "generator.properties";
    }

    public Map getConfigMap() {
        if (null == this.config) {
            this.config = Files.load(this.getConfigPropertiesFileName());
        }
        return this.config;
    }

    public String getDbUrl() {
        return (String) getConfigMap().get("db.url");
    }

    public String getDbUser() {
        return (String) getConfigMap().get("db.user");
    }

    public String getDbPassword() {
        return (String) getConfigMap().get("db.password");
    }

    public List<String> getTemplateDirs() {
        Set<Entry> set = getConfigMap().entrySet();
        List<String> dirs = new ArrayList<String>();
        for (Entry entry : set) {
            if (entry.getKey().toString().startsWith("templateDir")) {
                dirs.add((String) entry.getValue());
            }
        }
        return dirs;
    }

    public String getOutPutDir(String templateDir) {
        log.debug("li.generator.Config.getOutPutDir(?)", templateDir);
        Set<Entry> set = getConfigMap().entrySet();
        for (Entry entry : set) {
            if (entry.getValue().toString().startsWith(templateDir)) {
                String result = (String) getConfigMap().get(entry.getKey().toString().replace("templateDir", "outputDir")) //
                        + entry.getValue().toString().replace(templateDir, "");
                System.err.println(entry);
                log.debug("got out put dir " + result);
                return result;
            }
        }
        return "!@#$";
    }

    public String getTemplateSuffix() {
        return (String) getConfigMap().get("gen.suffix");
    }

    public String getTablePrefix() {
        return (String) getConfigMap().get("TABLE_PREFIX");
    }

    public String getTemplateName(File temp, String tempDir) throws Throwable {
        return temp.getCanonicalPath().replace(tempDir, "");
    }

    public String getEntityName(String tableName) {
        log.debug("li.generator.Config.getEntityName(" + tableName + ")");
        tableName = tableName.replace(this.getTablePrefix(), "");
        return tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
    }

    public String getOutPutFileName(String tempDir, Map configMap, File temp, String entityName) throws Throwable {
        if (temp.getCanonicalPath().contains(".java")) {
            return temp.getCanonicalPath().replace(tempDir, "").replace(this.getTemplateSuffix(), "").replace("$name", entityName);
        } else {
            return temp.getCanonicalPath().replace(tempDir, "").replace(this.getTemplateSuffix(), "").replace("$name", entityName).toLowerCase();
        }
    }

    public Boolean skipTable(String tableName) {
        return tableName.startsWith("r_");
    }
}