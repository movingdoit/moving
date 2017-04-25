package org.base.component.orm.hibernate;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.base.component.common.DalCondition;
import org.base.component.page.PageFinder;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;

public interface IHibernateBaseDao<T> {

    /**
     * 根据ID获取实体对象.
     * 
     * @param id
     *            记录ID
     * @return 实体对象
     */
    public T get(Serializable id);

    /**
     * 根据ID获取实体对象.
     * 
     * @param id
     *            记录ID
     * @return 实体对象
     */
    public T load(Serializable id);

    /**
     * 根据ID数组获取实体对象集合.
     * 
     * @param ids
     *            ID对象数组
     * 
     * @return 实体对象集合
     */
    public List<T> get(Serializable[] ids);

    /**
     * 根据属性名和属性值获取实体对象.
     * 
     * @param propertyName
     *            属性名称
     * @param value
     *            属性值
     * @return 实体对象
     */
    public T get(String propertyName, Object value);

    /**
     * 根据属性名和属性值获取实体对象集合.
     * 
     * @param propertyName
     *            属性名称
     * @param value
     *            属性值
     * @return 实体对象集合
     */
    public List<T> getList(String propertyName, Object value);

    /**
     * @Title: getList
     * @Description: 根据属性名和属性值数组获取实体对象集合.
     * @param propertyName
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-9上午10:01:49
     */
    public List<T> getList(String propertyName, Object[] values);

    /**
     * 获取所有实体对象集合.
     * 
     * @return 实体对象集合
     */
    public List<T> getAll();

    /**
     * 获取所有实体对象总数.
     * 
     * @return 实体对象总数
     */
    public Long getTotalCount();

    /**
     * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
     * 
     * @param propertyName
     *            属性名称
     * @param oldValue
     *            修改前的属性值
     * @param oldValue
     *            修改后的属性值
     * @return boolean
     */
    public boolean isUnique(String propertyName, Object oldValue, Object newValue);

    /**
     * 根据属性名判断数据是否已存在.
     * 
     * @param propertyName
     *            属性名称
     * @param value
     *            值
     * @return boolean
     */
    public boolean isExist(String propertyName, Object value);

    /**
     * 保存实体对象.
     * 
     * @param entity
     *            对象
     * @return ID
     */
    public Serializable save(T entity);

    /**
     * 更新实体对象.
     * 
     * @param entity
     *            对象
     */
    public void update(T entity);

    /**
     * 删除实体对象.
     * 
     * @param entity
     *            对象
     * @return
     */
    public void delete(T entity);

    /**
     * 根据ID删除实体对象.
     * 
     * @param id
     *            记录ID
     */
    public void delete(Serializable id);

    /**
     * 根据ID数组删除实体对象.
     * 
     * @param ids
     *            ID数组
     */
    public void delete(Serializable[] ids);

    /**
     * 
     * @Title: delete
     * @Description: 根据指定属性名和值删除记录
     * @param propertyName
     * @param value
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-4-2下午02:07:36
     */
    public void delete(String propertyName, Object value);

    public int delete(Map<String, Object> condition) throws Exception;

    /**
     * 清除某一对象.
     * 
     * @param object
     *            需要清除的对象
     */
    public void evict(Object object);

    public void flush();

    public void clear();

    // -------------------------------------------xieyong添加------------------------------
    /**
     * 保存或更新对象
     * 
     * @Title: saveOrUpdate
     * 
     * @param o
     * @return 返回保存的对象
     * @throws Exception
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17上午11:49:31
     */
    public T saveOrUpdate(T o);

    /**
     * 创建 Criteria
     * 
     * @Title: createCriteria
     * 
     * @param criterions
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午01:29:46
     */
    public Criteria createCriteria(Criterion... criterions);

    /**
     * 取得Entity的Criteria对象，带排序字段与升降序字段.
     * 
     * @param orderBy
     * @param isAsc
     * @param criterions
     * @return
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     */
    public Criteria createCriteria(String orderBy, boolean isAsc, Criterion... criterions);

    /**
     * 获得所有并排序
     * 
     * @Title: getAllByOrder
     * 
     * @param orderBy
     * @param isAsc
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午01:43:56
     */
    public List<T> getAllByOrder(String orderBy, boolean isAsc, boolean useCache);

    /**
     * 获得指定条数并排序
     * 
     * @Title: getLimitByOrder
     * 
     * @param orderBy
     * @param isAsc
     * @param limit
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午01:44:27
     */
    public List<T> getLimitByOrder(String orderBy, boolean isAsc, int limit, boolean useCache);

    /**
     * 获得总条数
     * 
     * @Title: getRowCount
     * 
     * @param criteria
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午01:45:00
     */
    public int getRowCount(Criteria criteria);

    /**
     * @Title: getListByCriteria
     * @Description: 根据标准查询器，查询数据
     * @param criteria
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-8下午3:05:37
     */
    public List<T> getListByCriteria(Criteria criteria);

    /**
     * 从指定行查询指定条数据
     * 
     * @Title: getListByCriteria
     * 
     * @param criteria
     * @param fistResult
     * @param maxResult
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午02:02:19
     */
    public List<T> getListByCriteria(Criteria criteria, int fistRow, int rowNum, boolean useCache);

    /**
     * 分页查询
     * 
     * @Title: pagedByCriteria
     * 
     * @param criteria
     * @param pageNo
     * @param pageSize
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午02:02:31
     */
    public PageFinder<T> pagedByCriteria(Criteria criteria, int pageNo, int pageSize);

    /**
     * 创建hqlQuery
     * 
     * @Title: createQuery
     * 
     * @param hql
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午02:12:56
     */
    public Query createQuery(String hql, Object... values);

    /**
     * 创建hqlQuery
     * 
     * @Title: createQuery
     * 
     * @param hql
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午02:13:40
     */
    public Query createQuery(String hql, Map<String, ?> values);

    /**
     * 创建sqlQuery
     * 
     * @Title: createSQLQuery
     * 
     * @param sql
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午02:14:01
     */
    public SQLQuery createSQLQuery(String sql, Object... values);

    /**
     * 创建sqlQuery
     * 
     * @Title: createSQLQuery
     * 
     * @param sql
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午02:15:39
     */
    public Query createSQLQuery(String sql, Map<String, ?> values);

    /*
     * /**
     * 
     * 根据 CritMap 条件查询单个对象
     * 
     * @Title: getObjectByCritMap
     * 
     * 
     * 
     * @param critMap
     * 
     * @param useCache
     * 
     * @return
     * 
     * @throws
     * 
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * 
     * @date: 2012-12-17下午03:41:42
     *//*
        * public T getObjectByCritMap(CritMap critMap,boolean ... useCache);
        * 
        * /** 根据 CritMap 条件查询列表
        * 
        * @Title: getListByCritMap
        * 
        * 
        * 
        * @param critMap
        * 
        * @param useCache
        * 
        * @return
        * 
        * @throws
        * 
        * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
        * 
        * @date: 2012-12-17下午03:43:37
        *//*
           * public List<T> getListByCritMap(CritMap critMap,boolean ...
           * useCache);
           * 
           * /** 根据 CritMap 条件查询列表
           * 
           * @Title: getListByCritMap
           * 
           * 
           * 
           * @param critMap
           * 
           * @param fistRow
           * 
           * @param rowNum
           * 
           * @param useCache
           * 
           * @return
           * 
           * @throws
           * 
           * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
           * 
           * @date: 2012-12-17下午03:45:32
           *//*
              * public List<T> getListByCritMap(CritMap critMap,int fistRow, int
              * rowNum,boolean ... useCache);
              * 
              * /** 根据 CritMap 条件分页查询
              * 
              * @Title: pagedByCritMap
              * 
              * 
              * 
              * @param critMap
              * 
              * @param No
              * 
              * @param pageSize
              * 
              * @return
              * 
              * @throws
              * 
              * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
              * 
              * @date: 2012-12-17下午03:45:44
              *//*
                 * public PageFinder<T> pagedByCritMap(CritMap critMap,int
                 * No,int pageSize);
                 */
    /**
     * 
     * 根据 CritMap 条件查询单个对象
     * 
     * @Title: getObjectByCritMap
     * 
     * @param critMap
     * @param useCache
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午03:41:42
     */
    public T getObjectByDalCondition(DalCondition conditionMap, boolean useCache);

    /**
     * 根据 CritMap 条件查询列表
     * 
     * @Title: getListByCritMap
     * 
     * @param critMap
     * @param useCache
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午03:43:37
     */
    public List<T> getListByDalCondition(DalCondition conditionMap, boolean useCache);

    /**
     * 根据 CritMap 条件查询列表
     * 
     * @Title: getListByCritMap
     * 
     * @param critMap
     * @param fistRow
     * @param rowNum
     * @param useCache
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午03:45:32
     */
    public List<T> getListByDalCondition(DalCondition conditionMap, int fistRow, int rowNum, boolean useCache);

    /**
     * 根据 CritMap 条件分页查询
     * 
     * @Title: pagedByCritMap
     * 
     * @param critMap
     * @param No
     * @param pageSize
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午03:45:44
     */
    public PageFinder<T> pagedByDalCondition(DalCondition conditionMap, int No, int pageSize);

    /**
     * 通过HQL得到一个对象
     * 
     * @Title: getObjectByHql
     * 
     * @param hql
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午07:38:52
     */
    public T getObjectByHql(String hql, Map<String, Object> values);

    /**
     * 通过HQL得到一个对象
     * 
     * @Title: getObjectByHql
     * 
     * @param hql
     * @param values
     * @return
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-7上午11:42:42
     */
    public T getObjectByHql(String hql, Object... values);

    /**
     * 通过HQL得到查询列表
     * 
     * @Title: getListByHql
     * 
     * @param hql
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午07:38:52
     */
    public List<T> getListByHql(String hql, Map<String, Object> values);

    /**
     * 通过HQL得到查询列表
     * 
     * @Title: getListByHql
     * 
     * @param hql
     * @param values
     * @return
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-7上午11:43:36
     */
    public List<T> getListByHql(String hql, Object... values);

    /**
     * 通过HQL得到查询列表
     * 
     * @Title: getListByHql
     * 
     * @param hql
     * @param values
     * @param firstRow
     * @param maxNum
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午07:42:42
     */
    public List<T> getListByHql(String hql, int firstRow, int maxNum, Map<String, Object> values);

    /**
     * 通过HQL得到查询列表
     * 
     * @Title: getListByHql
     * 
     * @param hql
     * @param firstRow
     * @param maxNum
     * @param values
     * @return
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-7上午11:46:18
     */
    public List<T> getListByHql(String hql, int firstRow, int maxNum, Object... values);

    /**
     * 分页查询:通过HQL,HQL不要包含子查询
     * 
     * @Title: pagedByHQL
     * 
     * @param hql
     * @param toPage
     * @param pageSize
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午07:42:48
     */
    public PageFinder<T> pagedByHQL(String hql, int toPage, int pageSize, Map<String, Object> values);

    /**
     * 分页查询:通过HQL,HQL不要包含子查询
     * 
     * @Title: pagedByHQL
     * 
     * @param hql
     * @param toPage
     * @param pageSize
     * @param values
     * @return
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-7上午11:47:05
     */
    public PageFinder<T> pagedByHQL(String hql, int toPage, int pageSize, Object... values);

    /**
     * 定制数据列表查询
     * 
     * @Title: getListByHQL
     * 
     * @param datasql
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午08:17:58
     */
    @SuppressWarnings("rawtypes")
    public List getListByHQL(String datasql, Map<String, Object> values);

    /**
     * 定制数据列表查询
     * 
     * @Title: getListByHQL
     * 
     * @param datasql
     * @param firstRow
     * @param maxNum
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午08:17:58
     */
    @SuppressWarnings("rawtypes")
    public List getListByHQL(String datasql, int firstRow, int maxNum, Map<String, Object> values);

    /**
     * 分页定制数据查询
     * 
     * @Title: pagedByHQLtoObject
     * 
     * @param countHql
     * @param hql
     * @param toPage
     * @param pageSize
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午07:43:26
     */
    public PageFinder<Object> pagedObjectByHQL(String countHql, String hql, int toPage, int pageSize,
            Map<String, Object> values);

    /**
     * 通过SQL语句列表查询
     * 
     * @Title: getListBySQL
     * @param datasql
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午08:18:31
     */
    public List<Object> getListBySQL(String datasql, Map<String, Object> values);

    /**
     * 通过SQL语句列表查询
     * 
     * @Title: getListBySQL
     * @param datasql
     * @param firstRow
     * @param maxNum
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午08:18:31
     */
    public List<Object> getListBySQL(String datasql, int firstRow, int maxNum, Map<String, Object> values);

    /**
     * 通过SQL语句分页查询
     * 
     * @Title: pagedBySQL
     * @param countsql
     * @param datasql
     * @param toPage
     * @param pageSize
     * @param values
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-17下午08:18:35
     */
    public PageFinder<Object> pagedObjectBySQL(String countsql, String datasql, int toPage, int pageSize,
            Map<String, Object> values);
    
    /**
     * 调用存储过程使用的方法
     * @param sql
     * @param inValues
     * @param outValues
     * @return
     */
	public CallableStatement excuteProc(String sql)throws HibernateException, SQLException;

}
