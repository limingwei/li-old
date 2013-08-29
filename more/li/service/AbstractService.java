package li.service;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import li.dao.AbstractDao;
import li.dao.Page;
import li.ioc.Ioc;
import li.util.Reflect;

/**
 * 一个Abstract的Service,用户可以继承并扩展它形成自己的Service
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.3 (2012-06-26)
 */

public abstract class AbstractService<T, ID extends Serializable> implements IBaseService<T, ID> {
    /**
     * Dao对象
     */
    private AbstractDao<T, ID> dao;

    /**
     * 你可以覆盖这个方法,如果不的话,框架会寻找 一个继承AbstractDao,泛型类型为 T的Bean
     * 
     * @see li.ioc.Ioc#get(Class, Type...)
     */
    protected AbstractDao<T, ID> getDao() {
        if (null == this.dao) {
            this.dao = Ioc.get(AbstractDao.class, Reflect.actualTypes(this.getClass()));
        }
        return this.dao;
    }

    /**
     * 删除
     */
    public Boolean delete(ID id) {
        return getDao().delete(id);
    }

    /**
     * 查找
     */
    public T find(ID id) {
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