package li.generator;

import java.io.File;
import java.util.Map;

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

    public String getTemplateDir() {
        return (String) getConfigMap().get("gen.templateDir");
    }

    public String getOutPutDir() {
        return (String) getConfigMap().get("gen.outputDir");
    }

    public String getTemplateSuffix() {
        return (String) getConfigMap().get("gen.suffix");
    }

    public String getTablePrefix() {
        return (String) getConfigMap().get("TABLE_PREFIX");
    }

    public String getTemplateName(File temp) throws Throwable {
        return temp.getCanonicalPath().replace(this.getTemplateDir(), "");
    }

    public String getEntityName(String tableName) {
        log.debug("li.generator.Config.getEntityName(" + tableName + ")");
        tableName = tableName.replace(this.getTablePrefix(), "");
        return tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
    }

    public String getOutPutFileName(Map configMap, File temp, String entityName) throws Throwable {
        if (temp.getCanonicalPath().contains(".java")) {
            return temp.getCanonicalPath().replace(this.getTemplateDir(), "").replace(this.getTemplateSuffix(), "").replace("$name", entityName);
        } else {
            return temp.getCanonicalPath().replace(this.getTemplateDir(), "").replace(this.getTemplateSuffix(), "").replace("$name", entityName).toLowerCase();
        }
    }

    public Boolean skipTable(String tableName) {
        return tableName.startsWith("r_");
    }
}