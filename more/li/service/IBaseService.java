package li.service;

import java.util.List;

import li.dao.Page;

/**
 * 基础Service接口,你可以继承并扩展它形成自己的Service接口
 * 
 * @author li (limw@w.cn)
 * @version 0.1.2 (2012-06-26)
 */
public interface IBaseService<T> {
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