package com.sightlyinc.ratecred.dao;

import java.util.List;

public interface BaseDao<T> {
	
	public T findByPrimaryKey(Long id);
	public List<T> findAll();
	public void delete(T entity);
	public Long save(T entity);
	public List<T> findByExample(T exampleInstance, String[] excludeProperty);

}
