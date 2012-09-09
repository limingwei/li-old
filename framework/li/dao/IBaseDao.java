package li.dao;

import java.util.List;

/**
 * 基础Dao接口,定义了几个基础的方法,用户可以继承并扩展它形成自己的Dao接口,当然,也可以不
 * 
 * @author li (limw@w.cn)
 * @version 0.1.2 (2012-06-26)
 */
public interface IBaseDao<T> {
	/**
	 * 删除
	 */
	public Boolean delete(Integer id);

	/**
	 * 查找
	 */
	public T find(Integer id);

	/**
	 * 保存
	 */
	public Boolean save(T t);

	/**
	 * 列表
	 */
	public List<T> list(Page page);

	/**
	 * 更新
	 */
	public Boolean update(T t);
}