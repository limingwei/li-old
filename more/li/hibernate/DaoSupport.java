package li.hibernate;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import li.dao.Page;
import li.util.Log;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.SingleTableEntityPersister;

/**
 * DaoSupport
 * 
 * @author 明伟
 */
public abstract class DaoSupport {
    private static final Log log = Log.init();

    /**
     * Pojo类型
     */
    private Class<?> entityClass;

    /**
     * Pojo名称
     */
    private String entityName;

    /**
     * Pojo对应表名
     */
    private String tableName;

    /**
     * Pojo ID 属性名
     */
    private String idField;

    /**
     * Pojo 对应表的 ID 字段名
     */
    private String idColumn;

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * getSessionFactory().getCurrentSession()
     */
    public Session getCurrentSession() {
        try {
            return getSessionFactory().getCurrentSession();
        } catch (HibernateException e) {
            if ("No Hibernate Session bound to thread, and configuration does not allow creation of non-transactional one here".equals(e.getMessage())) {
                log.info(e + "");
                return getSessionFactory().openSession();
            }
            throw e;
        }
    }

    /**
     * getDataSource
     */
    public DataSource getDataSource() {
        return ((li.hibernate.SessionFactory) sessionFactory).getDataSource();
    }

    /**
     * getConnection
     */
    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * getEntityClass
     */
    public Class<?> getEntityClass() {
        if (null == this.entityClass) {
            this.entityClass = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return this.entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * 返回对象名
     */
    public String getEntityName() {
        if (null == this.entityName) {
            this.entityName = this.getEntityClass().getSimpleName();
        }
        return this.entityName;
    }

    /**
     * 返回表名
     */
    public String getTableName() {
        if (null == this.tableName) {
            ClassMetadata classMetadata = this.getSessionFactory().getClassMetadata(this.getEntityClass());
            this.tableName = ((SingleTableEntityPersister) classMetadata).getTableName();
        }
        return this.tableName;
    }

    /**
     * 返回 Pojo ID 属性名
     */
    public String getIdField() {
        if (null == this.idField) {
            ClassMetadata classMetadata = this.getSessionFactory().getClassMetadata(this.getEntityClass());
            this.idField = classMetadata.getIdentifierPropertyName();
        }
        return this.idField;
    }

    /**
     * 返回 Pojo 对应表的 ID 字段名
     */
    public String getIdColumn() {
        if (null == this.idColumn) {
            ClassMetadata classMetadata = this.getSessionFactory().getClassMetadata(this.getEntityClass());
            this.idColumn = ((SingleTableEntityPersister) classMetadata).getIdentifierColumnNames()[0];
        }
        return this.idColumn;
    }

    /**
     * buildQuery
     * 
     * @param session 为空时使用 getCurrentSession()
     * @param page 为空时不分页
     * @param hql
     * @param args 替换?的参数数组
     */
    protected Query buildQuery(Session session, Page page, String hql, Object... args) {
        return setArgs(page, (null == session ? this.getCurrentSession() : session).createQuery(hql), args);
    }

    /**
     * buildSqlQuery
     * 
     * @param session 为空时使用 getCurrentSession()
     * @param page 为空时不分页
     * @param sql
     * @param args 替换?的参数数组
     */
    protected SQLQuery buildSqlQuery(Session session, Page page, String sql, Object... args) {
        return setArgs(page, (null == session ? this.getCurrentSession() : session).createSQLQuery(sql), args);
    }

    /**
     * setArgs #cannot define positional parameter after any named parameters have been defined
     * 
     * @param page null for no page
     * @param query hql
     * @param args map or not map
     */
    private <Q extends Query> Q setArgs(Page page, Q query, Object... args) {
        if (null != args) {
            int mapArgNum = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    query = (Q) this.setArgMap(query, (Map<String, Object>) args[i]);
                    mapArgNum++;
                } else {
                    query.setParameter(i - mapArgNum, args[i]);
                }
            }
        }
        if (null != page) {
            query.setFirstResult(page.getFrom());
            query.setMaxResults(page.getPageSize());
        }
        return query;
    }

    /**
     * setArgMap
     * 
     * @param query
     * @param args
     */
    private <Q extends Query> Q setArgMap(Q query, Map<String, Object> args) {
        Set<Entry<String, Object>> argsSet = args.entrySet();
        for (Entry<String, Object> arg : argsSet) {
            query.setParameter(arg.getKey(), arg.getValue());
        }
        return query;
    }
}
