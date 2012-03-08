package com.welocally.geodb.services.db;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.welocally.geodb.services.spatial.SpatialDocumentFactory;

@Component
public class DynamoJsonObjectFactory {
	
	static Logger logger = 
		Logger.getLogger(DynamoJsonObjectFactory.class);
	
	@Autowired SpatialDocumentFactory spatialDocumentFactory;
	
	
	public Map<String, AttributeValue> makePlace(JSONObject placeObject, String status) throws JSONException{
		
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		JSONObject properties = placeObject.getJSONObject("properties");
		JSONObject geom = placeObject.getJSONObject("geometry");
		JSONArray coords = geom.getJSONArray("coordinates");
		
		item.put("_id", new AttributeValue(placeObject.getString("_id")));
		item.put("lat", new AttributeValue().withN(coords.getString(1)));
		item.put("lng", new AttributeValue().withN(coords.getString(0)));
		item.put("search", new AttributeValue(spatialDocumentFactory.makeSearchablePlaceContent(properties)));
		item.put("status", new AttributeValue(status));
		item.put("document", new AttributeValue(placeObject.toString()));
		
        return item;
	}
	
	public Map<String, AttributeValue> makeClassifier(JSONObject jsonObject) throws JSONException{
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("_id", new AttributeValue(jsonObject.getString("_id")));
		item.put("type", new AttributeValue(jsonObject.getString("type")));
		item.put("category", new AttributeValue(jsonObject.getString("category")));
		if(!jsonObject.getString("subcategory").isEmpty())
			item.put("subcategory", new AttributeValue(jsonObject.getString("subcategory")));
		else
			item.put("subcategory", new AttributeValue(jsonObject.getString("category")));		
		
		return item;
	}
	
	public Map<String, AttributeValue> makeDeal(JSONObject jsonObject, String status) throws JSONException{
	    Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        
        JSONObject location = jsonObject.getJSONObject("location");
        
        JSONArray coordsNew =new JSONArray();
        coordsNew.put(new Double(location.getDouble("latitude")).toString());
        coordsNew.put(new Double(location.getDouble("longitude")).toString());

        
        item.put("_id", new AttributeValue(jsonObject.getString("_id")));
        item.put("lat", new AttributeValue().withN(new Double(location.getDouble("latitude")).toString()));
        item.put("lng", new AttributeValue().withN(new Double(location.getDouble("longitude")).toString()));
        item.put("search", new AttributeValue(spatialDocumentFactory.makeSearchableDealContent(jsonObject)));
        item.put("status", new AttributeValue(status));
        item.put("document", new AttributeValue(jsonObject.toString()));
        
        return item;
    }

}
