package org.base.component.orm.hibernate.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.base.component.common.BaseCondition.Entry;
import org.base.component.common.DalCondition;
import org.base.component.common.DalConditionType;
import org.base.component.common.DalConditionValue;
import org.base.component.orm.hibernate.IHibernateBaseDao;
import org.base.component.page.PageFinder;
import org.base.component.utils.ReflectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.util.Assert;


public class HibernateBaseDao<T> implements IHibernateBaseDao<T> {

	private final Class<T> entityClass;
	@Resource
	protected SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public HibernateBaseDao() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(this.getClass(), 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T load(Serializable id) {
		Assert.notNull(id, "id is required");
		return (T) this.getSession().load(this.entityClass, id);
	}

	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Serializable id) {
		Assert.notNull(id, "id is required");
		return (T) this.getSession().get(this.entityClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> get(Serializable[] ids) {
		
		Assert.notEmpty(ids, "ids must not be empty");
		String hql = "from " + this.entityClass.getName() + " as model where model.id in(:ids)";
		return this.getSession().createQuery(hql).setParameterList("ids", ids).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(String propertyName, Object value) {
		// Assert.hasText(propertyName, "propertyName must not be empty");
		// Assert.notNull(value, "value is required");
		if (value == null) {
			return null;
		}
		String hql = "from " + this.entityClass.getName() + " as model where model." + propertyName + " = ?";
		return (T) this.getSession().createQuery(hql).setParameter(0, value).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + this.entityClass.getName() + " as model where model." + propertyName + " = ?";
		return this.getSession().createQuery(hql).setParameter(0, value).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public List<T> getList(String propertyName, Object[] values) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(values, "values is required");
		String hql = "from " + this.entityClass.getName() + " as model where model." + propertyName + " in(:values)";
		return this.getSession().createQuery(hql).setParameterList("values", values).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public List<T> getAll() {
		String hql = "from " + this.entityClass.getName();
		return this.getSession().createQuery(hql).list();
	}

	@Override
	
	public Long getTotalCount() {
		String hql = "select count(*) from " + this.entityClass.getName();
		return Long.parseLong(this.getSession().createQuery(hql).uniqueResult().toString());
	}

	@Override
	
	public boolean isUnique(String propertyName, Object oldValue, Object newValue) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(newValue, "newValue is required");
		if (newValue == oldValue || newValue.equals(oldValue)) {
			return true;
		}
		if (newValue instanceof String) {
			if (oldValue != null && StringUtils.equalsIgnoreCase((String) oldValue, (String) newValue)) {
				return true;
			}
		}
		T object = this.get(propertyName, newValue);
		return object == null;
	}

	@Override
	
	public boolean isExist(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		T object = this.get(propertyName, value);
		return object != null;
	}

	@Override
	public Serializable save(T entity) {
		Assert.notNull(entity, "entity is required");
		return this.getSession().save(entity);
	}

	@Override
	public void update(T entity) {
		Assert.notNull(entity, "entity is required");
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$  session.merge()");
//		this.getSession().update(entity);
		this.getSession().merge(entity);
	}

	@Override
	public T saveOrUpdate(T o) {
		this.getSession().saveOrUpdate(o);
		return o;
	}

	@Override
	public void delete(T entity) {
		Assert.notNull(entity, "entity is required");
		this.getSession().delete(entity);
	}

	@Override
	public void delete(Serializable id) {
		Assert.notNull(id, "id is required");
		T entity = this.load(id);
		this.getSession().delete(entity);
	}

	@Override
	public void delete(Serializable[] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		for (Serializable id : ids) {
			T entity = this.load(id);
			this.getSession().delete(entity);
		}
	}

	@Override
	public void delete(String propertyName, Object value) {
		Assert.notNull(propertyName, "propertyName is required");
		Assert.notNull(value, "value is required");
		String hql = "delete from " + this.entityClass.getName() + " as model where model." + propertyName + " = ?";
		this.getSession().createQuery(hql).setParameter(0, value).executeUpdate();
	}

	@Override
	public int delete(Map<String, Object> conditions) throws Exception {
		if (null == conditions || conditions.isEmpty()) {
			throw new Exception("No conditions!");
		}

		StringBuffer hql = new StringBuffer("delete from " + this.entityClass.getName() + " as model ");
		if (null != conditions && conditions.size() > 0) {
			hql.append(" where ");

			int i = 1;
			Set<String> keySet = conditions.keySet();
			for (String key : keySet) {
				Object value = conditions.get(key);
				if (i > 1) {
					hql.append(" AND ");
				}
				if (value instanceof Collection<?> || value instanceof Object[]) {
					hql.append(" model." + key + " IN(:" + key + ") ");
				} else {
					hql.append(" model." + key + " = :" + key + " ");
				}
				++i;
			}
		}

		Query createQuery = this.getSession().createQuery(hql.toString());
		createQuery = this.setParameter(createQuery, conditions);
		return createQuery.executeUpdate();
	}

	@Override
	public void evict(Object object) {
		Assert.notNull(object, "object is required");
		this.getSession().evict(object);
	}

	@Override
	public void flush() {
		this.getSession().flush();
	}

	@Override
	public void clear() {
		this.getSession().clear();
	}

	@Override
	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = this.getSession().createCriteria(this.entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	@Override
	public Criteria createCriteria(String orderBy, boolean isAsc, Criterion... criterions) {
		Criteria criteria = this.createCriteria(criterions);
		if (isAsc) {
			criteria.addOrder(Order.asc(orderBy));
		} else {
			criteria.addOrder(Order.desc(orderBy));
		}

		return criteria;
	}

	@Override
	
	public List<T> getAllByOrder(String orderBy, boolean isAsc, boolean useCache) {
		return this.getLimitByOrder(orderBy, isAsc, -1, useCache);
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public List<T> getLimitByOrder(String orderBy, boolean isAsc, int limit, boolean useCache) {
		Assert.hasText(orderBy);

		Order order = isAsc ? Order.asc(orderBy) : Order.desc(orderBy);
		Criteria criteria = this.createCriteria();
		if (limit > 0) {
			criteria.setMaxResults(limit);
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(order).setCacheable(useCache);
		return criteria.list();
	}

	@Override
	public int getRowCount(Criteria criteria) {
		criteria.setProjection(Projections.rowCount());
		Long totalRows = Long.parseLong(criteria.uniqueResult().toString());
		return totalRows.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getListByCriteria(Criteria criteria) {
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getListByCriteria(Criteria criteria, int fistRow, int rowNum, boolean useCache) {
		criteria = criteria.setFirstResult(fistRow).setMaxResults(rowNum).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setCacheable(useCache);
		return criteria.list();
	}

	@Override
	
	public PageFinder<T> pagedByCriteria(Criteria criteria, int pageNo, int pageSize) {
		int totalRows = this.getRowCount(criteria);
		criteria.setProjection(null);
		if (totalRows < 1) {
			PageFinder<T> finder = new PageFinder<T>(pageNo, pageSize, totalRows);
			finder.setData(new ArrayList<T>());
			return finder;
		} else {
			PageFinder<T> finder = new PageFinder<T>(pageNo, pageSize, totalRows);
			List<T> list = this.getListByCriteria(criteria, finder.getStartOfPage(), finder.getPageSize(), false);
			finder.setData(list);
			return finder;
		}
	}

	/*
	 * 华龙备份，使用ConditionMap替代CritMap
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public T getObjectByCritMap(CritMap critMap,boolean ...
	 * useCache){ Criteria criteria =
	 * buildPropertyFilterCriteria(getSession().createCriteria
	 * (entityClass),critMap); if(useCache != null && useCache.length > 0 &&
	 * useCache[0]){ criteria.setCacheable(true); } return
	 * (T)criteria.uniqueResult(); }
	 * 
	 * @SuppressWarnings({ "static-access", "unchecked" })
	 * 
	 * @Override public List<T> getListByCritMap(CritMap critMap,boolean ...
	 * useCache){ Assert.notNull(critMap); Criteria criteria =
	 * getSession().createCriteria(entityClass);
	 * buildPropertyFilterCriteria(criteria,critMap);
	 * criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY); if(useCache
	 * != null && useCache.length > 0 && useCache[0]){
	 * criteria.setCacheable(true); } return criteria.list(); }
	 * 
	 * @SuppressWarnings({ "unchecked", "static-access" })
	 * 
	 * @Override public List<T> getListByCritMap(CritMap critMap,int fistRow,
	 * int rowNum,boolean ... useCache){ Assert.notNull(critMap); Criteria
	 * criteria = getSession().createCriteria(entityClass);
	 * buildPropertyFilterCriteria(criteria,critMap);
	 * criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
	 * criteria.setFirstResult(fistRow); criteria.setMaxResults(rowNum);
	 * if(useCache != null && useCache.length > 0 && useCache[0]){
	 * criteria.setCacheable(true); } return criteria.list(); }
	 * 
	 * @Override
	 * 
	 * @SuppressWarnings("unchecked") public PageFinder<T>
	 * pagedByCritMap(CritMap critMap,int pageNo,int pageSize) { Criteria
	 * criteria = getSession().createCriteria(entityClass);
	 * buildPropertyFilterCriteria(criteria,critMap); Integer totalRows =
	 * (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	 * criteria.setProjection(null); if (totalRows.intValue() < 1) { return new
	 * PageFinder<T>(pageNo, pageSize, totalRows.intValue()); } else {
	 * PageFinder<T> finder = new PageFinder<T>(pageNo, pageSize,
	 * totalRows.intValue()); List<T> list =
	 * criteria.setFirstResult(finder.getStartOfPage()).setMaxResults(
	 * finder.getPageSize
	 * ()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	 * finder.setData(list); return finder; } }
	 */

	@SuppressWarnings("unchecked")
	@Override
	
	public T getObjectByDalCondition(DalCondition dalCondition, boolean useCache) {
		Criteria criteria = this.getSession().createCriteria(this.entityClass);
		criteria = this.buildPropertyFilterCriteria(criteria, dalCondition);

		criteria.setCacheable(useCache);
		return (T) criteria.uniqueResult();
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	
	public List<T> getListByDalCondition(DalCondition dalCondition, boolean useCache) {
		Assert.notNull(dalCondition);

		Criteria criteria = this.getSession().createCriteria(this.entityClass);
		criteria = this.buildPropertyFilterCriteria(criteria, dalCondition);
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);

		criteria.setCacheable(useCache);
		return criteria.list();
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	
	public List<T> getListByDalCondition(DalCondition dalCondition, int fistRow, int rowNum, boolean useCache) {
		Assert.notNull(dalCondition);
		Criteria criteria = this.getSession().createCriteria(this.entityClass);
		criteria = this.buildPropertyFilterCriteria(criteria, dalCondition);
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult(fistRow);
		criteria.setMaxResults(rowNum);

		criteria.setCacheable(useCache);
		return criteria.list();
	}

	@Override
	
	public PageFinder<T> pagedByDalCondition(DalCondition dalCondition, int pageNo, int pageSize) {
		Criteria criteria = this.getSession().createCriteria(this.entityClass);
		criteria = this.buildPropertyFilterCriteria(criteria, dalCondition);
		int totalRows = this.getRowCount(criteria);
		criteria.setProjection(null);
		if (totalRows < 1) {
			return new PageFinder<T>(pageNo, pageSize, totalRows);
		} else {
			PageFinder<T> finder = new PageFinder<T>(pageNo, pageSize, totalRows);
			List<T> list = this.getListByCriteria(criteria, finder.getStartOfPage(), finder.getPageSize(), false);
			finder.setData(list);
			return finder;
		}
	}

	@Override
	public Query createQuery(String hql, Object... values) {
		Assert.hasText(hql, "sql 不能为空");
		Query query = this.getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	@Override
	public Query createQuery(String hql, Map<String, ?> values) {
		Assert.hasText(hql, "sql 不能为空");
		Query query = this.createQuery(hql);
		if (values != null) {
			query = this.setParameter(query, values);
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public T getObjectByHql(String hql, Map<String, Object> values) {
		Query query = this.createQuery(hql, values);
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public List<T> getListByHql(String hql, Map<String, Object> values) {
		Query query = this.createQuery(hql);
		query = this.setParameter(query, values);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public List<T> getListByHql(String hql, int firstRow, int maxNum, Map<String, Object> values) {
		Query query = this.createQuery(hql);
		query = this.setParameter(query, values);
		query.setFirstResult(firstRow);
		query.setMaxResults(maxNum);
		return query.list();
	}

	@Override
	
	public PageFinder<T> pagedByHQL(String hql, int toPage, int pageSize, Map<String, Object> values) {
		String countQueryString = " select count(*) " + this.removeSelect(this.removeOrders(hql));
		List<T> countlist = this.getListByHql(countQueryString, values);
		Long totalCount = (Long) countlist.get(0);

		if (totalCount.intValue() < 1) {
			return new PageFinder<T>(toPage, pageSize, totalCount.intValue());
		} else {
			final PageFinder<T> finder = new PageFinder<T>(toPage, pageSize, totalCount.intValue());
			List<T> list = this.getListByHql(hql, finder.getStartOfPage(), finder.getPageSize(), values);
			finder.setData(list);
			return finder;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	
	public List getListByHQL(String datasql, Map<String, Object> values) {
		Query dataQuery = this.createQuery(datasql, values);
		return dataQuery.list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	
	public List getListByHQL(String datasql, int firstRow, int maxNum, Map<String, Object> values) {
		Query dataQuery = this.createQuery(datasql, values);
		dataQuery.setFirstResult(firstRow);
		dataQuery.setMaxResults(maxNum);
		return dataQuery.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	
	public PageFinder<Object> pagedObjectByHQL(String countHql, String hql, int toPage, int pageSize, Map<String, Object> values) {
		Query query = this.createQuery(countHql, values);
		Long totalCount = Long.parseLong(query.uniqueResult().toString());
		if (totalCount.intValue() < 1) {
			return new PageFinder<Object>(toPage, pageSize, totalCount.intValue());
		} else {
			PageFinder<Object> finder = new PageFinder<Object>(toPage, pageSize, totalCount.intValue());
			List<Object> list = this.getListByHQL(hql, finder.getStartOfPage(), finder.getPageSize(), values);
			finder.setData(list);
			return finder;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	
	public T getObjectByHql(String hql, Object... values) {
		Query query = this.createQuery(hql, values);
		List<T> list = query.list();
		if (null != list && list.size() > 0) {
			T first = list.get(0);
			for (int i = 1; i < list.size(); i++) {
				if (list.get(i) != first) {
					throw new NonUniqueResultException(list.size());
				}
			}
			return first;
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	
	public List<T> getListByHql(String hql, Object... values) {
		Query dataQuery = this.createQuery(hql, values);
		return dataQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public List<T> getListByHql(String hql, int firstRow, int maxNum, Object... values) {
		Query query = this.createQuery(hql, values);
		query.setFirstResult(firstRow);
		query.setMaxResults(maxNum);
		return query.list();
	}

	@Override
	
	public PageFinder<T> pagedByHQL(String hql, int toPage, int pageSize, Object... values) {
		String countQueryString = " select count(*) " + this.removeSelect(this.removeOrders(hql));
		List<T> countlist = this.getListByHql(countQueryString, values);
		Long totalCount = (Long) countlist.get(0);

		if (totalCount.intValue() < 1) {
			return new PageFinder<T>(toPage, pageSize, totalCount.intValue());
		} else {
			final PageFinder<T> finder = new PageFinder<T>(toPage, pageSize, totalCount.intValue());
			List<T> list = this.getListByHql(hql, finder.getStartOfPage(), finder.getPageSize(), values);
			finder.setData(list);
			return finder;
		}
	}

	@Override
	public SQLQuery createSQLQuery(String sql, Object... values) {
		Assert.hasText(sql, "sql 不能为空");
		SQLQuery query = this.getSession().createSQLQuery(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	@Override
	public SQLQuery createSQLQuery(String sql, Map<String, ?> values) {
		Assert.hasText(sql, "sql 不能为空");
		Query query = this.createSQLQuery(sql);
		if (values != null) {
			query = this.setParameter(query, values);
		}
		return (SQLQuery) query;
	}
	
	@Override
	public CallableStatement excuteProc(String sql) throws HibernateException, SQLException {
		CallableStatement call = this.getSession().connection().prepareCall(sql);
		return call;
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public List<Object> getListBySQL(String datasql, Map<String, Object> values) {
		SQLQuery dataQuery = this.createSQLQuery(datasql, values);
		return dataQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	
	public List<Object> getListBySQL(String datasql, int firstRow, int maxNum, Map<String, Object> values) {
		SQLQuery dataQuery = this.createSQLQuery(datasql, values);
		dataQuery.setFirstResult(firstRow);
		dataQuery.setMaxResults(maxNum);
		return dataQuery.list();
	}

	@Override
	
	public PageFinder<Object> pagedObjectBySQL(String countsql, String datasql, int toPage, int pageSize, Map<String, Object> values) {
		SQLQuery query = this.createSQLQuery(countsql, values);
		Long totalCount = Long.parseLong(query.uniqueResult().toString());
		if (totalCount.intValue() < 1) {
			return new PageFinder<Object>(toPage, pageSize, totalCount.intValue());
		} else {
			PageFinder<Object> finder = new PageFinder<Object>(toPage, pageSize, totalCount.intValue());
			List<Object> list = this.getListBySQL(datasql, finder.getStartOfPage(), finder.getPageSize(), values);
			finder.setData(list);
			return finder;
		}
	}

	/**
	 * 取得对象的主键值,辅助函数.
	 */
	@SuppressWarnings("unused")
	private Serializable getId(Object entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Assert.notNull(entity);
		return (Serializable) PropertyUtils.getProperty(entity, this.getIdName());
	}

	/**
	 * 取得对象的主键名,辅助函数.
	 */
	private String getIdName() {
		ClassMetadata meta = this.sessionFactory.getClassMetadata(this.entityClass);
		Assert.notNull(meta, "Class " + this.entityClass + " not define in hibernate session factory.");
		String idName = meta.getIdentifierPropertyName();
		Assert.hasText(idName, this.entityClass.getSimpleName() + " has no identifier property define.");
		return idName;
	}

	/**
	 * hql 设置参数
	 * 
	 * @Title: setParameter
	 * @Description: TODO
	 * @param query
	 * @param map
	 * @return
	 * @throws
	 * @author: yong
	 * @date: 2012-12-17下午05:56:15
	 */
	private Query setParameter(Query query, Map<String, ?> map) {
		if (map != null && !map.isEmpty()) {
			Set<String> keySet = map.keySet();
			for (String string : keySet) {
				Object obj = map.get(string);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					query.setParameterList(string, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					query.setParameterList(string, (Object[]) obj);
				} else {
					query.setParameter(string, obj);
				}
			}
		}
		return query;
	}

	/**
	 * 去除hql的select 子句，未考虑union的情况用于pagedQuery.
	 * 
	 * @param hql
	 * @return
	 */
	private String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 * 
	 * @param hql
	 * @return
	 */
	private String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/*
	 * 华龙备份，使用ConditionMap替代CritMap /** 装配CritMap
	 * 
	 * @Title: buildPropertyFilterCriteria
	 * 
	 * @Description: TODO
	 * 
	 * @param criteria
	 * 
	 * @param critMap
	 * 
	 * @return
	 * 
	 * @throws
	 * 
	 * @author: yong
	 * 
	 * @date: 2012-12-17下午02:28:10\/ private Criteria
	 * buildPropertyFilterCriteria(Criteria criteria,CritMap critMap) { try {
	 * Map<String, Object> propertyMap = null; //带别名级联 propertyMap =
	 * critMap.getFieldMap(MatchType.ALIASFECH.name()); if(propertyMap != null){
	 * for (String key : propertyMap.keySet()) { //key 为别名 value 为属性名 String
	 * value = propertyMap.get(key).toString(); criteria.createAlias(value,
	 * key); criteria.setFetchMode(key, FetchMode.JOIN); } } //级联 propertyMap =
	 * critMap.getFieldMap(MatchType.FECH.name()); if(propertyMap != null){ for
	 * (String key : propertyMap.keySet()) { //key 为别名 value 为属性名 String value =
	 * propertyMap.get(key).toString(); criteria.setFetchMode(value,
	 * FetchMode.JOIN); } } //相等 propertyMap =
	 * critMap.getFieldMap(MatchType.EQUAL.name()); if(propertyMap != null){ for
	 * (String key : propertyMap.keySet()) { Object value =
	 * propertyMap.get(key); criteria.add(Restrictions.eq(key, value)); } }
	 * //左模糊查询 propertyMap = critMap.getFieldMap(MatchType.L_LIKE.name());
	 * if(propertyMap != null){ for (String key : propertyMap.keySet()) { Object
	 * value = propertyMap.get(key);
	 * criteria.add(Restrictions.like(key,value+"%")); } } //右模糊查询 propertyMap =
	 * critMap.getFieldMap(MatchType.R_LIKE.name()); if(propertyMap != null){
	 * for (String key : propertyMap.keySet()) { Object value =
	 * propertyMap.get(key); criteria.add(Restrictions.like(key,"%"+value)); } }
	 * //模糊查询 propertyMap = critMap.getFieldMap(MatchType.LIKE.name());
	 * if(propertyMap != null){ for (String key : propertyMap.keySet()) { Object
	 * value = propertyMap.get(key);
	 * criteria.add(Restrictions.like(key,"%"+value+"%")); } } //大于 propertyMap
	 * = critMap.getFieldMap(MatchType.GREATER.name()); if(propertyMap != null){
	 * for (String key : propertyMap.keySet()) { Object value =
	 * propertyMap.get(key); criteria.add(Restrictions.gt(key,value)); } }
	 * //大于等于 propertyMap = critMap.getFieldMap(MatchType.GREATER_EQUAL.name());
	 * if(propertyMap != null){ for (String key : propertyMap.keySet()) { Object
	 * value = propertyMap.get(key); criteria.add(Restrictions.ge(key,value)); }
	 * } //小于 propertyMap = critMap.getFieldMap(MatchType.LESS.name());
	 * if(propertyMap != null){ for (String key : propertyMap.keySet()) { Object
	 * value = propertyMap.get(key); criteria.add(Restrictions.lt(key,value)); }
	 * } //小于等于 propertyMap = critMap.getFieldMap(MatchType.LESS_EQUAL.name());
	 * if(propertyMap != null){ for (String key : propertyMap.keySet()) { Object
	 * value = propertyMap.get(key); criteria.add(Restrictions.le(key,value)); }
	 * } //IN //增加IN类型查询，用于同一属性多值查询　by dsy 20110421_1639 propertyMap =
	 * critMap.getFieldMap(MatchType.IN.name()); if(propertyMap != null){ for
	 * (String key : propertyMap.keySet()) { Object[] values = (Object[])
	 * propertyMap.get(key); criteria.add(Restrictions.in(key, values)); } }
	 * //升序 propertyMap = critMap.getFieldMap(MatchType.ORDER_ASC.name());
	 * if(propertyMap != null){ for (String key : propertyMap.keySet()) {
	 * criteria.addOrder(Order.asc(key)); } } //降序 propertyMap =
	 * critMap.getFieldMap(MatchType.ORDER_DESC.name()); if(propertyMap !=
	 * null){ for (String key : propertyMap.keySet()) {
	 * criteria.addOrder(Order.desc(key)); } } //获取指定数据数 int maxsize =
	 * critMap.getMaxSize(); if(maxsize > 0){ criteria.setMaxResults(maxsize); }
	 * } catch (Exception e) { throw
	 * ReflectionUtils.convertToUncheckedException(e); } return criteria; }
	 */

	/**
	 * 装配CritMap
	 * 
	 * @Title: buildPropertyFilterCriteria
	 * @Description: TODO
	 * @param criteria
	 * @param conditionMap
	 * @return
	 * @throws
	 * @author: yong
	 * @date: 2012-12-17下午02:28:10
	 */
	private Criteria buildPropertyFilterCriteria(Criteria criteria, DalCondition conditionMap) {
		// 循环取出条件值
		List<Entry<String, DalConditionValue>> conditions = conditionMap.getConditionValues();
		if (null != conditions) {
			try {
				for (Entry<String, DalConditionValue> condition : conditions) {

					String propName = DalCondition.getPropName(condition.getK());
					DalConditionValue cv = condition.getV();// 获取条件值
					DalConditionType type = cv.getConditionType();// 获取属性值
					switch (type) {
					/* 条件排序 */
					case ORDER_ASC:
						List<String> orderAscList = cv.getOrderAsc();
						if (null != orderAscList) {
							for (String field : orderAscList) {
								criteria.addOrder(Order.asc(field));
							}
						}
						break;
					case ORDER_DESC:
						List<String> orderDescList = cv.getOrderDesc();
						if (null != orderDescList) {
							for (String field : orderDescList) {
								criteria.addOrder(Order.desc(field));
							}
						}
						break;
//					/* 条件之间的关系 */ //20151110,因实际没使用，所以去掉
//					  case OR:
//	                        Object[] or = cv.getOr();
//	                        if (null != or) {
//	                            criteria.add(Restrictions.or(this.getCondition((DalConditionType) or[0], (String) or[1],
//	                                    or[2])));
//	                        }
//	                        break;
					default:
						criteria.add(this.getCondition(type, propName, cv.getValue()));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	private Criterion getCondition(DalConditionType type, String propName, Object value) throws Exception {
		Criterion criterion = null;
		switch (type) {
		/* 数字、字符串、日期条件 */
		case EQUAL:
			criterion = Restrictions.eq(propName, value);
			break;
		case NOT_EQUAL:
			criterion = Restrictions.ne(propName, value);
			break;
		case IN:
			criterion = Restrictions.in(propName, ((List<Object>) value).toArray());
			break;
		case NOT_IN:
			criterion = Restrictions.not(Restrictions.in(propName, ((List<Object>) value).toArray()));
			break;
		/* 字符串条件的关系符 */
		case LIKE:
			criterion = Restrictions.like(propName, (String) value, MatchMode.ANYWHERE);
			break;
		case NOT_LIKE:
			criterion = Restrictions.not(Restrictions.like(propName, (String) value, MatchMode.ANYWHERE));
			break;
		case LLIKE:
			criterion = Restrictions.like(propName, (String) value, MatchMode.START);
			break;
		case NOT_LLIKE:
			criterion = Restrictions.not(Restrictions.like(propName, (String) value, MatchMode.START));
			break;
		case RLIKE:
			criterion = Restrictions.like(propName, (String) value, MatchMode.END);
			break;
		case NOT_RLIKE:
			criterion = Restrictions.not(Restrictions.like(propName, (String) value, MatchMode.END));
			break;
		/* 数字、日期、字符、条件的关系符 */
		case LESS:
			criterion = Restrictions.lt(propName, value);
			break;
		case LESS_EQUAL:
			criterion = Restrictions.le(propName, value);
			break;
		case GREATER:
			criterion = Restrictions.gt(propName, value);
			break;
		case GREATER_EQUAL:
			criterion = Restrictions.ge(propName, value);
			break;
		case BETWEEN_AND:
			Object bav[] = (Object[]) value;
			criterion = Restrictions.between(propName, bav[0], bav[1]);
			break;
		case NOT_BETWEEN_AND:
			Object nbav[] = (Object[]) value;
			criterion = Restrictions.not(Restrictions.between(propName, nbav[0], nbav[1]));
			break;
		}
		return criterion;
	}
}
