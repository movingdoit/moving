package org.base.component.orm.jpa.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.base.component.orm.jpa.IJpaBaseDao;

public class JpaBaseDao<T> implements IJpaBaseDao<T>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T get(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T load(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> get(Serializable[] ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getList(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTotalCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUnique(String propertyName, Object oldValue,
			Object newValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExist(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Serializable save(T entity) {
		this.entityManager.persist(entity);
		return null;
	}

	@Override
	public void update(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Serializable id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Serializable[] ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T find(Class<T> clazz, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persist(T t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Query createQuery(String jpql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalCount(String jpql) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<T> find(String jpql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> find(String jpql, Object param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> find(String jpql, Object... param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(T t) {
		// TODO Auto-generated method stub
		
	}

	
}
