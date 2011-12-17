package com.welocally.geodb.services.db;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

@Component
public class MongoJsonDatabase implements JsonDatabase {
	
	static Logger logger = 
		Logger.getLogger(MongoJsonDatabase.class);
	
	private Mongo mongo;

	private DB db;
		
	@Value("${jsonDatabase.pageSize:10}")
	private int pageSize;
	
	@PostConstruct
	public void initDb(){
		try {
			mongo = new Mongo( "localhost" , 27017 );
			db = mongo.getDB( "geodb" );
		} catch (UnknownHostException e) {
			logger.error("problem init of db:"+e.getMessage(), e);
		} catch (MongoException e) {
			logger.error("problem init of db:"+e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.welocally.geodb.services.db.JsonDatabase#findById(java.lang.String, java.lang.String)
	 */
	public JSONObject findById(String collectionName, String id) throws DbException {
		
		JSONObject dbObjectJson = null;
		
		try {
			DBCollection coll = 
				db.getCollection(collectionName);
			
			BasicDBObject query = new BasicDBObject();
			query.put("_id", id);

			DBObject obj = coll.findOne(query);
			
			dbObjectJson = new JSONObject(obj.toString());
			
		} catch (MongoException e) {
			throw new DbException(e);
		} catch (JSONException e) {
			throw new DbException(e);
		}

		return dbObjectJson;
	}

	/**
	 * function printStudents(pageNumber, nPerPage) {
   print("Page: " + pageNumber);
   db.students.find().skip((pageNumber-1)*nPerPage).limit(nPerPage).forEach( function(student) { print(student.name + "<p>"); } );
}
	 */
	@Override
	public DbPage findAll(String collectionName, int pageNumber) throws DbException {
		DbPage pageResult = new DbPage();

		try {
			DBCollection coll = 
				db.getCollection(collectionName);
			
			DBCursor cur = 
				coll.find().skip((pageNumber-1)*this.pageSize).limit(pageSize);
			int total = cur.count();
			pageResult.setCount(total);
			
			pageResult.setPageNum(pageNumber);

	        while(cur.hasNext()) {
	            DBObject obj = cur.next();
	            pageResult.getObjects().put(new JSONObject(obj.toString()));
	        }
	        
	        pageResult.setPageSize(pageResult.getObjects().length());
			
		} catch (MongoException e) {
			throw new DbException(e);
		} catch (JSONException e) {
			throw new DbException(e);
		}
				
		return pageResult;
	}
	
	

}
