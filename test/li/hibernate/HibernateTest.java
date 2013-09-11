package li.hibernate;

import li.annotation.Inject;
import li.hibernate.test.User;
import li.test.BaseTest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class HibernateTest extends BaseTest {
    @Inject
    SessionFactory sessionFactory;

    @Test
    public void test() throws Exception {
        Session session = sessionFactory.openSession();
        System.err.println(session.load(User.class, 1));

        User user = new User();
        user.setUsername("123");
        user.setPassword("123");
        user.setEmail("eee");
        user.setTel("ttt");

        System.err.println(session.save(user));

        Query query = session.createQuery("FROM User");
        System.err.println(JSON.toJSON(query.list()));
    }
}