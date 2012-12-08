package li.service;

import java.lang.reflect.Type;
import java.util.List;

import li.dao.AbstractDao;
import li.dao.IBaseDao;
import li.dao.Page;
import li.ioc.Ioc;
import li.util.Reflect;

/**
 * 一个Abstract的Service,用户可以继承并扩展它形成自己的Service
 * 
 * @author li (limw@w.cn)
 * @version 0.1.3 (2012-06-26)
 */

public abstract class AbstractService<T> implements IBaseService<T> {
    /**
     * Dao对象
     */
    private IBaseDao<T> dao;

    /**
     * 你可以覆盖这个方法,如果不的话,框架会寻找 一个继承AbstractDao,泛型类型为 T的Bean
     * 
     * @see li.ioc.Ioc#get(Class, Type)
     */
    protected IBaseDao<T> getDao() {
        if (null == this.dao) {
            this.dao = Ioc.get(AbstractDao.class, Reflect.actualType(this.getClass(), 0));
        }
        return this.dao;
    }

    /**
     * 删除
     */
    public Boolean delete(Number id) {
        return getDao().delete(id);
    }

    /**
     * 查找
     */
    public T find(Number id) {
        return getDao().find(id);
    }

    /**
     * 列表
     */
    public List<T> list(Page page) {
        return getDao().list(page);
    }

    /**
     * 保存
     */
    public Boolean save(T t) {
        return getDao().save(t);
    }

    /**
     * 更新
     */
    public Boolean update(T t) {
        return getDao().update(t);
    }
}