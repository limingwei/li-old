package li.demo.dao.impl;

import li.annotation.Bean;
import li.dao.AbstractDao;
import li.demo.dao.IUserDao;
import li.demo.model.User;

/**
 * 你的Dao可以继承自泛型的AbstractDao并扩展更多的方法
 */
@Bean
public class UserDaoImpl extends AbstractDao<User> implements IUserDao {
    public User findByEmail(String email) {
        return super.find("WHERE email=?", email);// 问号占位符的查询参数
    }

    public User findByUsername(String username) {
        return super.find("WHERE username=?", username);// 具名占位符的查询参数
    }
}