package com.welocally.geodb.services.db;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodb.model.AttributeValue;

@Component
public class DynamoJsonObjectFactory {
	
	/*
	 * {
    "_id": "WL_5dPhlN9PsTrqBREtYhL5CO_37.807605_-122.268846@1293134755",
    "geo_distance": 5.64487307280821,
    "properties": {
        "tags": [
            "sandwich",
            "eating"
        ],
        "phone": "+1 510 839 7295",
        "classifiers": [
            {
                "category": "Restaurant",
                "subcategory": "",
                "type": "Food & Drink"
            }
        ],
        "address": "435 19th St",
        "name": "Lana's Sandwiches",
        "province": "CA",
        "owner": "welocally",
        "postcode": "94612",
        "city": "Oakland",
        "country": "US"
    },
    "type": "Place",
    "geometry": {
        "type": "Point",
        "coordinates": [
            -122.268997,
            37.8078
        ]
    }
}
	 */
	public Map<String, AttributeValue> makePlace(JSONObject placeObject, String status) throws JSONException{
		
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		JSONObject properties = placeObject.getJSONObject("properties");
		JSONObject geom = placeObject.getJSONObject("geometry");
		JSONArray coords = geom.getJSONArray("coordinates");
		
		item.put("_id", new AttributeValue(placeObject.getString("_id")));
		item.put("lat", new AttributeValue().withN(coords.getString(1)));
		item.put("lng", new AttributeValue().withN(coords.getString(0)));
		item.put("search", new AttributeValue(makeSearchableContent(properties)));
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


	
	public String makeSearchableContent(JSONObject placeProperties) throws JSONException{
		StringBuffer buf = new StringBuffer();
		buf.append(placeProperties.getString("name")+" ");
		
		//classifiers
		if(placeProperties.has("classifiers")){
			JSONArray categories = placeProperties.getJSONArray("classifiers");
			for (int i = 0; i < categories.length(); i++) {
				//buf.append(tags.getString(i)+" ");
				JSONObject cat = categories.getJSONObject(i);
				buf.append(cat.getString("category")+" ");
				buf.append(cat.getString("subcategory")+" ");
				buf.append(cat.getString("type")+" ");
			}
		}
		
		//tags
		if(placeProperties.has("tags")){
			JSONArray tags = placeProperties.getJSONArray("tags");
			for (int i = 0; i < tags.length(); i++) {
				buf.append(tags.getString(i)+" ");
			}
		}
		
		return buf.toString();	
	}

}
