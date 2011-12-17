package com.sightlyinc.ratecred.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sightlyinc.ratecred.dao.BaseDao;

@Transactional
public abstract class AbstractTransactionalService<T> implements BaseService<T> {
	
	public abstract BaseDao<T> getDao();

	@Override
	public void delete(T entity) {
		getDao().delete(entity);		
	}

	@Override
	public List<T> findAll() {
		return getDao().findAll();
	}

	@Override
	public T findByPrimaryKey(Long id) {
		return getDao().findByPrimaryKey(id);
	}

	@Override
	public Long save(T entity) {
		return getDao().save(entity);
	}

}
