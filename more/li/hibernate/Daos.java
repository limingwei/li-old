package li.hibernate;

import org.hibernate.SessionFactory;

/**
 * @author 明伟
 */
public class Daos {
    /**
     * @param _sessionFactory
     * @param _entityClass
     */
    public static <T> AbstractDao<T, Integer> getDao(final SessionFactory _sessionFactory, final Class<T> _entityClass) {
        return new AbstractDao<T, Integer>() {
            public SessionFactory getSessionFactory() {
                return _sessionFactory;
            }

            public Class<?> getEntityClass() {
                return _entityClass;
            }
        };
    }
}