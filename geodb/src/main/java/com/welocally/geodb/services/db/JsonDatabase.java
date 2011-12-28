package com.welocally.geodb.services.db;

import java.util.List;

import org.json.JSONObject;

public interface JsonDatabase {

	public JSONObject findById(String collectionName, String id)
		throws DbException;
	
	public void put(JSONObject doc, String collectionName, String id)
		throws DbException;
	
	public DbPage findAll(String collectionName, int pageNum) throws DbException;
	
	public List<Object> findDistinct(String collectionName, String key, JSONObject query)
		throws DbException;
	
	public DbPage findByExample(String collectionName, int pageNumber, JSONObject example)
		throws DbException;
	
	public DbPage findByExampleIncluding(String collectionName, int pageNumber, JSONObject example, JSONObject inclusions)
	throws DbException;

}