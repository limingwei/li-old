package li.generator;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import li.util.Log;

/**
 * Generator
 * 
 * @author 明伟
 */
public class Generator {
    static Log log = Log.init();

    static Config config = new Config();

    public static void main(String[] args) throws Throwable {
        List<String> tableNames = getTableNames();
        for (String tableName : tableNames) {
            if (!config.skipTable(tableName)) {
                for (String templateDir : config.getTemplateDirs()) {
                    generate(tableName, templateDir);
                }
            }
        }
    }

    private static void generate(String table, String templateDir) throws Throwable {
        log.trace("li.generator.Generator.generate(" + table + ", " + templateDir + ")");
        generateDir(table, new File(templateDir), templateDir);
    }

    private static void generateFile(String tableName, File temp, String templateDir) throws Throwable {
        if (temp.getName().endsWith(config.getTemplateSuffix())) {
            Map values = getValues(tableName, templateDir);
            FreemarkerUtil.generateFile(config, temp, values);
        }
    }

    private static Map getValues(String tableName, String tempDir) {
        Map map = config.getConfigMap();
        map.put("tableName", tableName);
        map.put("entityName", config.getEntityName(tableName));
        map.put("templateDir", tempDir);

        map.put("outPutDir", config.getOutPutDir(tempDir));
        log.debug("out put dir is " + config.getOutPutDir(tempDir));
        return map;
    }

    private static void generateDir(String table, File dir, String tempDir) throws Throwable {
        log.trace("li.generator.Generator.generateDir(" + table + ", " + dir + ")");
        File[] files = dir.listFiles();
        if (null != files) {
            for (File file : files) {
                if (file.isDirectory()) {
                    generateDir(table, file, tempDir);
                } else if (file.isFile()) {
                    generateFile(table, file, tempDir);
                }
            }
        }
    }

    private static List<String> getTableNames() throws Throwable {
        log.trace("li.generator.Generator.getTableNames()");
        List<String> tableNames = new ArrayList<String>();
        Connection connection = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword());

        PreparedStatement preparedStatement = connection.prepareStatement("show tables");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(1));
        }
        return tableNames;
    }
}