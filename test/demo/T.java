package demo;

import li.dao._User;
import li.ioc.Ioc;

import com.alibaba.fastjson.JSON;

public class T {
    public static void main(String[] args) throws Exception {
        _User userDao = Ioc.get(_User.class);

        System.out.println(userDao.list(null, "select * from framework_forum.t_account"));
        System.out.println(userDao.list(null, "select * from mysql.db"));
        System.out.println(userDao.list(null, "select version()"));
        System.out.println(userDao.list(null, "select database()"));
        System.out.println(userDao.list(null, "select * from discuz_x25_sc_utf8.dx25su_forum_post"));

        System.out.println(JSON.toJSONString(userDao.list(null)));

        userDao.testDesc();
    }
}