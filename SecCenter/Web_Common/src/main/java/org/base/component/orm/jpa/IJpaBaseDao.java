package org.base.component.orm.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

/**
 * 
 * @ClassName: IJpaBaseDao 
 * @Description: TODO
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-1-8 下午4:14:17
 *
 */
public interface IJpaBaseDao<T> {
	
	/**
	 * 
	 * @Title: clear 
	 * @Description: 清除一级缓存的数据  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:15:08
	 */
	public void clear();
	
	/**
	 * 
	 * @Title: get 
	 * @Description: 根据ID获取实体对象
	 * @param id	记录ID
	 * @return  	实体对象
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:17:42
	 */
	public T get(Serializable id);

	/**
	 * 
	 * @Title: load 
	 * @Description: 根据ID获取实体对象
	 * @param id	记录ID
	 * @return  	实体对象
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:18:30
	 */
	public T load(Serializable id);
	
	/**
	 * 
	 * @Title: get 
	 * @Description: 根据ID数组获取实体对象集合
	 * @param ids	ID对象数组
	 * @return  	实体对象集合
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:18:59
	 */
	public List<T> get(Serializable[] ids);
	
	/**
	 * 
	 * @Title: get 
	 * @Description: 根据属性名和属性值获取实体对象
	 * @param propertyName	属性名称
	 * @param value			属性值
	 * @return  			实体对象
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:19:27
	 */
	public T get(String propertyName, Object value);
	
	/**
	 * 
	 * @Title: getList 
	 * @Description: 根据属性名和属性值获取实体对象集合
	 * @param propertyName	属性名称
	 * @param value			属性值
	 * @return  			实体对象集合
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:20:01
	 */
	public List<T> getList(String propertyName, Object value);

	/**
	 * 
	 * @Title: getAll 
	 * @Description: 获取所有实体对象集合
	 * @return  	实体对象集合
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:20:32
	 */
	public List<T> getAll();
	
	/**
	 * 
	 * @Title: getTotalCount 
	 * @Description: 获取所有实体对象总数
	 * @return  	实体对象总数
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:21:30
	 */
	public Long getTotalCount();

	/**
	 * 
	 * @Title: isUnique 
	 * @Description: 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true)
	 * @param propertyName	属性名称
	 * @param oldValue		修改前的属性值
	 * @param newValue		修改后的属性值
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:21:53
	 */
	public boolean isUnique(String propertyName, Object oldValue, Object newValue);
	
	/**
	 * 
	 * @Title: isExist 
	 * @Description: 根据属性名判断数据是否已存在
	 * @param propertyName	属性名称
	 * @param value			属性值
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:22:38
	 */
	public boolean isExist(String propertyName, Object value);

	/**
	 * 
	 * @Title: save 
	 * @Description: 保存实体对象
	 * @param entity	实例对象
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:23:14
	 */
	public Serializable save(T entity);

	/**
	 * 
	 * @Title: update 
	 * @Description: 更新实体对象
	 * @param entity  	实例对象
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:28:30
	 */
	public void update(T entity);
	
	/**
	 * 
	 * @Title: delete 
	 * @Description: 删除实体对象
	 * @param entity  	实例对象
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:28:53
	 */
	public void delete(T entity);

	/**
	 * 
	 * @Title: delete 
	 * @Description: 根据ID删除实体对象
	 * @param id  		记录ID
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:29:08
	 */
	public void delete(Serializable id);

	/**
	 * 
	 * @Title: delete 
	 * @Description: 根据ID数组删除实体对象
	 * @param ids  		ID数组
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-8下午4:29:53
	 */
	public void delete(Serializable[] ids);
	
	public T find(Class<T> clazz,Serializable id);
	
	public void persist(T t);
	
	public Query createQuery(String jpql);
	
	public int getTotalCount(String jpql);
	
	public List<T> find(String jpql);
	
	public List<T> find(String jpql, Object param);
	
	public List<T> find(String jpql, Object... param);
	
	public void saveOrUpdate(T t);
}
