package li.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import li.dao.Page;

import org.hibernate.criterion.CriteriaSpecification;

/**
 * AbstractDao  
 * 
 * @author : 明伟 
 * @date : 2013年8月1日 下午4:55:46
 * @version 1.0 
 */
public abstract class AbstractDao<T, ID extends Serializable> extends DaoSupport {
    /**
     * list
     */
    public List<T> list(Page page) {
        return super.buildQuery(null, page, " FROM " + this.getEntityName() + " ").list();
    }

    /**
     * listByProperty
     */
    public List<T> listByProperty(String propertyName, Object value, Page page) {
        return this.list(page, "FROM " + this.getEntityName() + " WHERE " + propertyName + "=?", value);
    }

    /**
     * listByHql
     */
    public List<T> list(Page page, String hql, Object... args) {
        return super.buildQuery(null, page, hql, args).list();
    }

    /**
     * listBySql
     */
    public List<T> listBySql(Page page, String sql, Object... args) {
        return super.buildSqlQuery(null, page, sql, args).addEntity(this.getEntityClass()).list();
    }

    /**
     * count
     */
    public Integer count() {
        return this.count("SELECT COUNT(*) FROM  " + this.getEntityName());
    }

    /**
     * countByHql
     */
    public Integer count(String hql, Object... args) {
        return ((Number) super.buildQuery(null, null, hql, args).uniqueResult()).intValue();
    }

    /**
     * countBySql
     */
    public Integer countBySql(String sql, Object... args) {
        return ((Number) super.buildSqlQuery(null, null, sql, args).uniqueResult()).intValue();
    }

    /**
     * find
     */
    public T find(ID id) {
        return (T) super.getCurrentSession().get(this.getEntityClass(), id);
    }

    /**
     * find
     */
    public T find(String hql, Object... args) {
        return (T) super.buildQuery(null, null, hql, args).uniqueResult();
    }

    /**
     * 使用Sql查询数据库,返回map
     * 
     * @param sql
     * @param args
     * @return
     */
    public Map<?, ?> findMap(String sql, Object... args) {
        return (Map<?, ?>) super.buildSqlQuery(null, null, sql, args).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).uniqueResult();
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
        return super.buildSqlQuery(null, page, sql, args).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * findBySql
     */
    public T findBySql(String sql, Object... args) {
        return (T) super.buildSqlQuery(null, null, sql, args).addEntity(this.getEntityClass()).uniqueResult();
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
    public Boolean delete(T entry) {
        try {
            super.getCurrentSession().delete(entry);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
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

    /**
     * save
     */
    public Boolean save(T entry) {
        try {
            super.getCurrentSession().save(entry);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * saveOrUpdate
     */
    public Boolean saveOrUpdate(T entry) {
        try {
            super.getCurrentSession().saveOrUpdate(entry);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * update
     */
    public Boolean update(T entity) {
        try {
            super.getCurrentSession().update(entity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * 更新操作
     */
    public Integer update(String hql, Object... args) {
        return super.buildQuery(null, null, hql, args).executeUpdate();
    }

    /**
     * updateBySql
     */
    public Integer updateBySql(String sql, Object... args) {
        return super.buildSqlQuery(null, null, sql, args).executeUpdate();
    }
}