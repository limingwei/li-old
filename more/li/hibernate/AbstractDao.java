package li.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import li.dao.Page;

import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.SingleTableEntityPersister;

/**
 * AbstractDao  
 * 
 * @author : 明伟 
 * @date : 2013年8月1日 下午4:55:46
 * @version 1.0 
 */
public abstract class AbstractDao<T, ID extends Serializable> extends DaoSupport {
    private static final Page SIZE_ONE_PAGE = new Page().setPageSize(1).count(false);

    /**
     * listByHql
     */
    public List<T> list(Page page, String hql, Object... args) {
        Session session = super.getOrOpenSession();
        List<T> list = super.buildQuery(session, page, hql, args).list();
        super.closeSession(session);
        return list;
    }

    /**
     * listBySql
     */
    public List<T> listBySql(Page page, String sql, Object... args) {
        Session session = super.getOrOpenSession();
        List<T> list = super.buildSqlQuery(session, page, sql, args).list();
        super.closeSession(session);
        return list;
    }

    /**
     * countByHql
     */
    public Integer count(String hql, Object... args) {
        Session session = super.getOrOpenSession();
        Integer count = ((Number) super.buildQuery(session, null, hql, args).uniqueResult()).intValue();
        super.closeSession(session);
        return count;
    }

    /**
     * countBySql
     */
    public Integer countBySql(String sql, Object... args) {
        Session session = super.getOrOpenSession();
        Integer count = ((Number) super.buildSqlQuery(session, null, sql, args).uniqueResult()).intValue();
        super.closeSession(session);
        return count;
    }

    /**
     * 使用Sql查询数据库,返回mapList
     * 
     * @param page
     * @param sql
     * @param args
     * @return
     */
    public List<Map<?, ?>> listMap(Page page, String sql, Object... args) {
        Session session = super.getOrOpenSession();
        List<Map<?, ?>> list = super.buildSqlQuery(session, page, sql, args).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
        super.closeSession(session);
        return list;
    }

    /**
     * 更新操作
     */
    public Integer update(String hql, Object... args) {
        Session session = super.getOrOpenSession();
        Integer result = super.buildQuery(session, null, hql, args).executeUpdate();
        super.closeSession(session);
        return result;
    }

    /**
     * updateBySql
     */
    public Integer updateBySql(String sql, Object... args) {
        Session session = super.getOrOpenSession();
        Integer result = super.buildSqlQuery(session, null, sql, args).executeUpdate();
        super.closeSession(session);
        return result;
    }

    /**
     * save
     */
    public Boolean save(T entry) {
        Session session = super.getOrOpenSession();
        try {
            session.save(entry);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        } finally {
            super.closeSession(session);
        }
    }

    /**
     * saveOrUpdate
     */
    public Boolean saveOrUpdate(T entry) {
        Session session = super.getOrOpenSession();
        try {
            session.saveOrUpdate(entry);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        } finally {
            super.closeSession(session);
        }
    }

    /**
     * update
     */
    public Boolean update(T entity) {
        Session session = super.getOrOpenSession();
        try {
            session.update(entity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        } finally {
            super.closeSession(session);
        }
    }

    /**
     * delete
     */
    public Boolean delete(T entity) {
        Session session = super.getOrOpenSession();
        try {
            session.delete(entity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        } finally {
            super.closeSession(session);
        }
    }

    /**
     * findBySql
     */
    public T findBySql(String sql, Object... args) {
        List<T> list = this.listBySql(SIZE_ONE_PAGE, sql, args);
        return null == list || list.isEmpty() ? null : list.get(0);
    }

    /**
     * find
     */
    public T find(String hql, Object... args) {
        List<T> list = this.list(SIZE_ONE_PAGE, hql, args);
        return null == list || list.isEmpty() ? null : list.get(0);
    }

    /**
     * 使用Sql查询数据库,返回map
     * 
     * @param sql
     * @param args
     * @return
     */
    public Map<?, ?> findMap(String sql, Object... args) {
        List<Map<?, ?>> list = this.listMap(SIZE_ONE_PAGE, sql, args);
        return null == list || list.isEmpty() ? null : list.get(0);
    }

    /**
     * find
     */
    public T find(ID id) {
        return this.find("FROM " + this.getEntityName() + " WHERE " + this.getIdField() + "=?", id);
    }

    /**
     * list
     */
    public List<T> list(Page page) {
        return this.list(page, "FROM " + this.getEntityName());
    }

    /**
     * listByProperty
     */
    public List<T> listByProperty(String propertyName, Object value, Page page) {
        return this.list(page, "FROM " + this.getEntityName() + " WHERE " + propertyName + "=?", value);
    }

    /**
     * count
     */
    public Integer count() {
        return this.count("SELECT COUNT(*) FROM  " + this.getEntityName());
    }

    /**
     * delete
     */
    public Boolean delete(ID id) {
        return 0 < this.update("DELETE FROM " + this.getEntityName() + " WHERE " + super.getIdField() + " = ? ", id);
    }

    /**
     * delete
     */
    public Integer delete(String hql, Object... args) {
        return this.update(hql, args);
    }

    /**
     * deleteBySql
     */
    public Integer deleteBySql(String sql, Object... args) {
        return this.updateBySql(sql, args);
    }

    public void saveIgnoreNull(T entity) {
        ClassMetadata classMetadata = super.getSessionFactory().getClassMetadata(this.getEntityClass());
        SingleTableEntityPersister singleTableEntityPersister = (SingleTableEntityPersister) classMetadata;
        singleTableEntityPersister.getEntityMetamodel().isDynamicInsert();
        singleTableEntityPersister.getEntityMetamodel().isDynamicUpdate();
    }
}