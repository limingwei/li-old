package li.generator;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import li.util.Files;
import li.util.Log;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Generator {
    private static final Properties config = Files.load("generator.properties");
    private static final String db_url = config.getProperty("db.url");
    private static final String db_user = config.getProperty("db.user");
    private static final String db_password = config.getProperty("db.password");

    private static final String templateDir = config.getProperty("gen.templateDir");
    private static final String outputDir = config.getProperty("gen.outputDir");
    private static final String suffix = config.getProperty("gen.suffix");

    static Log log = Log.init();

    public static void main(String[] args) throws Throwable {
        List<String> tableNames = getTableNames();
        for (String table : tableNames) {
            generate(table, templateDir);
        }
    }

    private static void generate(String table, String templateDir) throws Throwable {
        log.trace("li.generator.Generator.generate(" + table + ", " + templateDir + ")");
        generateDir(table, new File(templateDir));
    }

    private static void generateFile(String table, File temp) throws Throwable {
        if (temp.getName().endsWith(suffix)) {
            log.trace("li.generator.Generator.generateFile(" + table + ", " + temp + ")");
            Configuration configuration = new Configuration();
            configuration.setTemplateLoader(new FileTemplateLoader(new File(templateDir)));
            Template template = configuration.getTemplate(getTemplateName(temp));
            Map map = new HashMap();
            File file = new File(outputDir + "//" + getOutFileName(config, temp, getEntityName(table)));
            file.getParentFile().mkdirs();
            template.process(map, new FileWriter(file));
        }
    }

    private static String getOutFileName(Properties config, File temp, String entityName) throws Throwable {
        log.trace("li.generator.Generator.getOutFileName(Properties, " + temp + ")");
        return temp.getCanonicalPath().replace(templateDir, "").replace(suffix, "").replace("$name", entityName);
    }

    private static String getEntityName(String tableName) {
        return tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
    }

    private static String getTemplateName(File temp) throws Throwable {
        return temp.getCanonicalPath().replace(templateDir, "");
    }

    private static void generateDir(String table, File dir) throws Throwable {
        log.trace("li.generator.Generator.generateDir(" + table + ", " + dir + ")");
        File[] files = dir.listFiles();
        if (null != files) {
            for (File file : files) {
                if (file.isDirectory()) {
                    generateDir(table, file);
                } else if (file.isFile()) {
                    generateFile(table, file);
                }
            }
        }
    }

    private static List<String> getTableNames() throws Throwable {
        log.trace("li.generator.Generator.getTableNames()");
        List<String> tableNames = new ArrayList<String>();
        Connection connection = DriverManager.getConnection(db_url, db_user, db_password);

        PreparedStatement preparedStatement = connection.prepareStatement("show tables");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(1));
        }
        return tableNames;
    }
}