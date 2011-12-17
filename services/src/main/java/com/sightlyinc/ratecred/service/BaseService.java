package com.sightlyinc.ratecred.service;

import java.util.List;

public interface BaseService<T> {
	
	public T findByPrimaryKey(Long id);
	public List<T> findAll();
	public void delete(T entity);
	public Long save(T entity);

}
