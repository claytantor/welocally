package com.welocally.geodb.services.util;

import java.io.IOException;
import java.io.StringWriter;

import org.json.JSONException;
import org.json.JSONObject;

import com.welocally.geodb.services.db.DbException;

public interface JsonStoreLoader {

	public abstract void load(String fileName, int maxRecords, int commitEvery,
	        String endpoint) throws DbException;

	public abstract void loadSingle(JSONObject place, int count,
	        int commitEvery, StringWriter sw) throws JSONException, IOException;

}