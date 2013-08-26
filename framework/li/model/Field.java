package li.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import li.util.Log;
import li.util.Verify;

/**
 * 属性对象,表示一个Bean的一个属性,或者Table的一个列
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.7 (2012-05-08)
 */
public class Field {
    private static final Log log = Log.init();

    /**
     * 属性名
     */
    public String name;

    /**
     * 属性的JAVA类型
     */
    public Class<?> type;

    /**
     * 这个属性的值
     */
    public String value;

    /**
     * 属性对应数据库表的列名
     */
    public String column;

    /**
     * 通过扫描对象结构及注解的方式得到一个类型的属性列表 List<Field>,根据类名缓存
     * 
     * @param targetType 目标对象
     * @param annotated 是否只列出有Field注解的字段
     */
    public static List<Field> list(Class<?> targetType, Boolean annotated) {
        List<Field> fields = (List<Field>) Log.get("~!@#FIELDS_MAP#CLASS#" + targetType.getName() + "#ANNOTATED#" + annotated);
        if (null == fields) { // 如果缓存中没有
            log.debug("Field.list() by type ?", targetType.getName());
            fields = new ArrayList<Field>();
            java.lang.reflect.Field[] declaredFields = targetType.getDeclaredFields();
            for (java.lang.reflect.Field field : declaredFields) {
                li.annotation.Field column = field.getAnnotation(li.annotation.Field.class);
                if (!annotated || null != column) {// 如果不需要Field注解或者Field注解不为空
                    li.model.Field attribute = new li.model.Field();
                    attribute.name = field.getName();
                    attribute.column = (null == column || Verify.isEmpty(column.value())) ? field.getName() : column.value();
                    fields.add(attribute);
                }
            }
            if (Object.class != targetType.getSuperclass()) {// 扫描超类的Field
                fields.addAll(list(targetType.getSuperclass(), annotated));
            }
            Log.put("~!@#FIELDS_MAP#CLASS#" + targetType.getName() + "#ANNOTATED#" + annotated, fields); // 加入缓存
        }
        return fields;
    }

    /**
     * 通过ResultSetMetaData的方式得到一个类型(表)的属性(字段)列表 List<Field>,根据表名缓存
     * 
     * @param dataSource 通过ResultSetMetaData解析表结构时候需要用到的数据源
     * @param table 需要探测表结构的数据表名称
     */
    public static List<Field> list(DataSource dataSource, String table) {
        List<Field> fields = (List<Field>) Log.get("~!@#FIELDS_MAP#DATASOURCE#" + dataSource + "#TABLE#" + table);
        if (null == fields) { // 如果缓存中没有
            log.debug("Field.list() by table ?", table);
            try {
                fields = new ArrayList<Field>();
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE 1=2");
                ResultSet resultSet = preparedStatement.executeQuery();
                fields = list(resultSet);
                if (null != resultSet) {// ?
                    resultSet.close();// 关闭resultSet
                }
                preparedStatement.close();
                connection.close();// 关闭connection,QueryRunner中可能因为事务没有关闭之
                Log.put("~!@#FIELDS_MAP#DATASOURCE#" + dataSource + "#TABLE#" + table, fields); // 加入缓存
            } catch (Exception e) {
                throw new RuntimeException(null == dataSource ? " A dataSource in Ioc is needed, like https://github.com/limingwei/li/blob/master/demo/demo-config.xml " : e + " ", e);
            }
        }
        return fields;
    }

    /**
     * 通过ResultSetMetaData的方式得到一个结果集的列的列表
     */
    public static List<Field> list(ResultSet resultSet) {
        List<Field> fields = new ArrayList<Field>();
        try {
            ResultSetMetaData meta = (null == resultSet ? null : resultSet.getMetaData());
            for (int columnCount = (null == meta ? -1 : meta.getColumnCount()), i = 1; i <= columnCount; i++) {
                Field attribute = new Field();
                attribute.name = attribute.column = meta.getColumnLabel(i);
                fields.add(attribute);
            }
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
        return fields;
    }
}
