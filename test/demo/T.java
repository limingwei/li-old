package demo;

import java.util.List;

import javax.sql.DataSource;

import li.dao.QueryBuilder2;
import li.dao.SimpleDataSource;
import li.dao._User;
import li.ioc.Ioc;
import li.model.Field;
import li.util.Reflect;

import com.alibaba.fastjson.JSON;

public class T {
    private static final String ALIAS_SIGN = "#";
    private static final DataSource dataSource = new SimpleDataSource("jdbc:mysql://127.0.0.1/framework_forum", "root", "");

    public static void main(String[] args) {
        String sql1 = "SELECT t_account.*,t_forum.# as f_#,t_member.*,p.# p_# FROM t_account,t_member,t_forum,t_post p";
        String sql2 = "SELECT m.# AS m_# FROM t_member m";
        System.out.println(setAlias(sql1) + "\n" + setAlias(sql2));
    }

    public static String setAlias(String sql) {
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

            System.out.println(old_part);

            String fix = old_part.substring(old_part.indexOf(' '), old_part.length() - 1);

            List<Field> fields = Field.list(dataSource, table_name);
            for (Field field : fields) {// 构造替换字符串
                new_part = new_part + (table_alias + '.' + field.column) + (fix + field.column) + ',';// 构造一列加AS
            }
            String result = sql.replaceFirst(old_part, new_part.substring(0, new_part.length() - 1));// 替换,去掉最后一个逗号
            return setAlias(result);// 处理下一个
        }
    }

    public static void main3(String[] args) {
        QueryBuilder2 queryBuilder = new QueryBuilder2();
        Reflect.set(queryBuilder, "dataSource", Ioc.get(DataSource.class));

        String sql = "SELECT t_account.*,t_member.# AS member_# FROM t_account,t_member";

        String sql2 = "SELECT t_account.*," + //
                "t_member.id member_id,t_member.name member_name," + //
                "t_member.account_id member_account_id,t_member.status member_status " + //
                "FROM t_account,t_member";//
        System.out.println(sql + "\n" + sql2 + "\n" + queryBuilder.setAlias(sql) + "\n" + queryBuilder.setAlias(sql).equals(sql2));
    }

    public static void main2(String[] args) throws Exception {
        _User userDao = Ioc.get(_User.class);

        System.out.println(userDao.list(null, "select * from framework_forum.t_account"));
        System.out.println(userDao.list(null, "select * from mysql.db"));
        System.out.println(userDao.list(null, "select version()"));
        System.out.println(userDao.list(null, "select database()"));
        System.out.println(userDao.list(null, "select * from discuz_x25_sc_utf8.dx25su_forum_post"));

        System.out.println(JSON.toJSONString(userDao.list(null)));

        userDao.testDesc();

        System.out.println(userDao.list(null, "DESC t_account"));

        System.out.println(userDao.list(null, "where 1=1"));
        System.out.println(userDao.list(null, "where true"));
    }
}