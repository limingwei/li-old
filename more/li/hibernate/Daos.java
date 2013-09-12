package li.hibernate;

import java.util.List;

import li.dao.Page;

import org.hibernate.SessionFactory;

/**
 * @author 明伟
 */
public class Daos {
    /**
     * list
     */
    public static <T> List<T> list(SessionFactory sessionFactory, Class<T> entityClass, Page page) {
        return getDao(sessionFactory, entityClass).list(page);
    }

    /**
     * listByHql
     */
    public static <T> List<T> list(SessionFactory sessionFactory, Class<T> entityClass, Page page, String hql, Object... args) {
        return getDao(sessionFactory, entityClass).list(page, hql, args);
    }

    /**
     * listBySql
     */
    public static <T> List<T> listBySql(SessionFactory sessionFactory, Class<T> entityClass, Page page, String sql, Object... args) {
        return getDao(sessionFactory, entityClass).listBySql(page, sql, args);
    }

    /**
     * count
     */
    public Integer count(SessionFactory sessionFactory, Class<?> entityClass) {
        return getDao(sessionFactory, entityClass).count();
    }

    /**
     * countByHql
     */
    public Integer count(SessionFactory sessionFactory, Class<?> entityClass, String hql, Object... args) {
        return getDao(sessionFactory, entityClass).count(hql, args);
    }

    /**
     * countBySql
     */
    public Integer countBySql(SessionFactory sessionFactory, Class<?> entityClass, String sql, Object... args) {
        return getDao(sessionFactory, entityClass).countBySql(sql, args);
    }

    /**
     * @param hibernateTemplate
     * @param entityClass
     */
    private static AbstractDao getDao(final SessionFactory _sessionFactory, final Class<?> _entityClass) {
        return new AbstractDao() {
            public SessionFactory getSessionFactory() {
                return _sessionFactory;
            }

            public Class<?> getEntityClass() {
                return _entityClass;
            }
        };
    }
}