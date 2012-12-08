package li.dao;

import java.util.List;

/**
 * 基础Dao接口,定义了几个基础的方法,可以继承并扩展它形成自己的Dao接口
 * 
 * @author li (limw@w.cn)
 * @version 0.1.2 (2012-06-26)
 */
public interface IBaseDao<T> {
    /**
     * 删除
     */
    public Boolean delete(Number id);

    /**
     * 查找
     */
    public T find(Number id);

    /**
     * 列表
     */
    public List<T> list(Page page);

    /**
     * 保存
     */
    public Boolean save(T t);

    /**
     * 更新
     */
    public Boolean update(T t);
}