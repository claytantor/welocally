package com.welocally.geodb.services.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.welocally.geodb.services.app.CommandException;
import com.welocally.geodb.services.app.CommandSupport;
import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.DbPage;
import com.welocally.geodb.services.db.IdGen;
import com.welocally.geodb.services.db.JsonDatabase;

@Component
public class MongoCategoryLoader implements CommandSupport {
	
	

	static Logger logger = 
		Logger.getLogger(MongoCategoryLoader.class);
	
	@Autowired JsonDatabase jsonDatabase;
	
	@Autowired IdGen idGen;
	

	@Override
	public void doCommand(JSONObject command) throws CommandException {
		try {
			logger.debug("doCommand");
			List<Object> types =
				jsonDatabase.findDistinct(
						command.getString("source"), 
						"properties.classifiers.type", null);
			for (Object type : types) {
				String name = type.toString();
				if(name != null && name.length()>0){
					logger.debug("making category:"+type.toString());
					String cid = loadCategoryElement(type.toString(), null,command.getString("target"), "Type");
					//loadCategories(type.toString(), cid,command.getString("source"),command.getString("target"));					
				}
			}
						
		} catch (DbException e) {
			logger.error(e);
			throw new CommandException(e);
		} catch (JSONException e) {
			logger.error(e);
			throw new CommandException(e);
		}
	
	}
	
	/*
	 * db.places.find(
		{"properties.classifiers.type":"Services"},
		{_id:0,geometry:0,type:0,"properties.owner":0,
			"properties.phone":0,"properties.name":0,
			"properties.province":0,"properties.country":0,
			"properties.postcode":0,"properties.city":0,
			"properties.website":0,"properties.phone":0,
			"properties.address":0,owner:0,"properties.tags":0})

	 */
	
	private void loadClassifiersForType(
			String typeName, String parentId, 
			String sourceCollection, String targetCollection) 
	throws DbException, JSONException {
		
		
			JSONObject example = new JSONObject("{\"properties.classifiers.type\":\""+typeName+"\"}");
			
			JSONObject inclusions  = new JSONObject("{_id:0,\"properties.classifiers\":1}");
			
			DbPage dbpage =
				jsonDatabase.findByExampleIncluding(sourceCollection, 1,  example,  inclusions  );
			
			
			int resultCount = 0;
			int maxDocs = 100;
			if (dbpage.getCount() > dbpage.getObjects().length()) {
				int pages = dbpage.getCount() / dbpage.getPageSize();
				if (dbpage.getCount() % dbpage.getPageSize() > 0)
					pages = pages + 1;
				for (int pageNumIndex = 2; pageNumIndex <= pages && resultCount<maxDocs; pageNumIndex++) {
					dbpage = jsonDatabase.findByExample("categories", pageNumIndex, example);
					resultCount = resultCount + dbpage.getObjects().length();
					for (int i = 0; i < dbpage.getObjects().length(); i++) {
						//allCats.put(dbpage.getObjects().get(i));
					}
				}
			}
			
			
		
	}
	
	private void loadCategories(String parentCategoryName, String parentId, String sourceCollection, String targetCollection) 
	throws DbException, JSONException{
	
	JSONObject example = new JSONObject();
	example.put("properties.classifiers.type", parentCategoryName);
	
		List<Object> cats = jsonDatabase.findDistinct(sourceCollection,
				"properties.classifiers.category", example);
		for (Object cat : cats) {
			String name = cat.toString();		
			
			if(name != null && name.length()>0){
				logger.debug("making category:"+name+" child of type:"+parentCategoryName);
				String cid = loadCategoryElement(name, parentId, targetCollection, "Category");
				loadSubcategories(name, cid, sourceCollection, targetCollection);
			}
			
		}
	}
	
	
	private void loadSubcategories(String parentCategoryName, String parentId, String sourceCollection, String targetCollection) 
		throws DbException, JSONException{
		
		JSONObject example = new JSONObject();
		example.put("properties.classifiers.category", parentCategoryName);
		
		List<Object> subcats =
			jsonDatabase.findDistinct(sourceCollection, "properties.classifiers.subcategory", example);
		for (Object subcat : subcats) {
			String name = subcat.toString();
			logger.debug("making subcategory:"+name+" child of:"+parentCategoryName);
			if(name != null && name.length()>0){
				loadCategoryElement(name, parentId, targetCollection, "Subcategory");
			}
		}
	}
	

	public String loadCategoryElement(String name, String parentId, String targetCollection, String type) 
	throws JSONException, DbException{
		String id = idGen.genBasic(10);
		JSONObject cat = new JSONObject();
		cat.put("name", name);
		cat.put("type", type);
		if(parentId != null){
			cat.put("parentId", parentId);
		}		
		jsonDatabase.put(cat, targetCollection, id);
		return id;
	}
	
	public boolean clasifierExists(String type, String category, String subcategory, String targetCollection){
		return false;
	}
	
	public String loadClassifierElement(String type, String category, String subcategory, String targetCollection) 
	throws JSONException, DbException{
//		String id = idGen.genBasic(10);
//		JSONObject cat = new JSONObject();
//		cat.put("name", name);
//		cat.put("type", type);
//		if(parentId != null){
//			cat.put("parentId", parentId);
//		}		
//		jsonDatabase.put(cat, targetCollection, id);
//		return id;
		
		return null;
	}

}
