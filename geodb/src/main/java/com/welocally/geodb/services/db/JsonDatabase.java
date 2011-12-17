package com.welocally.geodb.services.db;

import org.json.JSONObject;

public interface JsonDatabase {

	public abstract JSONObject findById(String collectionName, String id)
		throws DbException;
	
	public abstract DbPage findAll(String collectionName, int pageNum) throws DbException;

}