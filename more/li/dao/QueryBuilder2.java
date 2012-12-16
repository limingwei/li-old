package li.dao;

import java.util.List;

import li.model.Field;
import li.util.Files;
import li.util.Log;
import li.util.Verify;

public class QueryBuilder2 extends QueryBuilder {
    private static final String ALIAS_SIGN = Files.load("config.properties").getProperty("dao.aliasSign", "#");

    /**
     * 处理批量别名,将类似 SELECT t_account.*,t_member.# member_# FROM t_account,t_member的SQL转换为标准SQL
     */
    /**
     * #####加一个内存缓存,一定要
     */
    public String setAlias(String sql) {
        Object sqlBack = Log.get(sql + dataSource);// 从缓存中查找
        if (!Verify.isEmpty(sqlBack)) {
            return (String) sqlBack;
        } else {
            int index = sql.indexOf('.' + ALIAS_SIGN) + 2;// .#
            if (index < 8) {
                return sql;// 如果SQL中不包含.#,则直接返回
            } else {// 如果有替换字符,则开始处理
                int old_part_start_1 = sql.substring(0, index).lastIndexOf(' ');// #之前最后一个空格的位置
                int old_part_start_2 = sql.substring(0, index).lastIndexOf(',');// #之前最后一个,的位置
                int old_part_start = (old_part_start_1 > old_part_start_2 ? old_part_start_1 : old_part_start_2) + 1;// 取靠后者

                int old_part_end = sql.indexOf(ALIAS_SIGN, index + 1) + 1;// 第一个标记后的第一个标记

                String old_part = sql.substring(old_part_start, old_part_end);// 求出被替换部分字符串
                String new_part = "";// 申明替换部分字符串

                String table_alias = sql.substring(old_part_start, index - 2);// 求得表名

                int from_index = sql.toUpperCase().lastIndexOf(" FROM ");
                int table_alias_index = sql.indexOf(" " + table_alias, from_index);

                String table_name = table_alias_index <= (from_index + 6) ? table_alias : sql.substring(from_index + 6, table_alias_index);

                String fix = old_part.substring(old_part.indexOf(' '), old_part.length() - 1);

                List<Field> fields = Field.list(dataSource, table_name);
                for (Field field : fields) {// 构造替换字符串
                    new_part = new_part + (table_alias + '.' + field.column) + (fix + field.column) + ',';// 构造一列加AS
                }
                String result = sql.replaceFirst(old_part, new_part.substring(0, new_part.length() - 1));// 替换,去掉最后一个逗号

                Log.put(sql + dataSource, result);// 缓存起来
                return setAlias(result);// 处理下一个
            }
        }
    }
}
