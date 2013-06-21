package li.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import li.annotation.Table;
import li.dao.Record;
import li.util.Log;
import li.util.Verify;

/**
 * Bean对象,表示一个IocBean或TableBean
 * 
 * @author li (limw@w.cn)
 * @version 0.1.6 (2012-05-08)
 */
public class Bean {
    /**
     * Bean的一个实例
     */
    public Object instance;

    /**
     * Bean的名称
     */
    public String name;

    /**
     * Bean的类型
     */
    public Class type;

    /**
     * Bean的属性集合
     */
    public List<Field> fields = new ArrayList<Field>();

    /**
     * TableBean的数据库表名
     */
    public String table;

    /**
     * 这个Bean的Id,从getId()方法得到
     */
    private Field id;

    /**
     * 得到这个对象的ID,Field类型,有缓存的
     */
    public Field getId() {
        if (null == this.id) {
            throw new RuntimeException("no primary key for " + table);
        }
        return this.id;
    }

    /**
     * 根据注解等方式得到一个类型的结构,其中包含属性列表和tableName等,根据类名缓存
     * 
     * @param dataSource 通过DESC tableName解析表结构时候需要用到的数据源
     * @param type 目标类型
     */
    public static Bean getMeta(DataSource dataSource, Class<?> type) {
        Bean bean = (Bean) Log.get("BEAN_MAP#" + type);
        if (null == bean) {
            bean = new Bean();
            Table table = type.getAnnotation(Table.class);
            bean.table = (null == table || Verify.isEmpty(table.value())) ? type.getSimpleName() : table.value();
            bean.fields = Record.class.isAssignableFrom(type) ? Field.list(dataSource, bean.table) : Field.list(type, true);// 若type为Record的子类型,则用扫描数据表方式,否则用扫描对象方式
            for (Field attribute : bean.fields) {
                if (table.id().equals(attribute.name) || table.id().equals(attribute.column)) {
                    bean.id = attribute;
                    break;
                }
            }
            Log.put("BEAN_MAP#" + type, bean);
        }
        return bean;
    }
}