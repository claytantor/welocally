package com.welocally.geodb.services.spatial;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.welocally.geodb.services.index.DocumentContentException;

@Component
public class SpatialDocumentFactory {
	

//    
	/**
	 * {
    "_id": "SG_4mgIoipAcMRxxAbUX8b5Ls_-12.523856_131.043243@1303236333",
    "properties": {
        "tags": [
            "upholsterer"
        ],
        "phone": "+61 8 8983 4134",
        "classifiers": [
            {
                "category": "Retail",
                "subcategory": "Repair Shop",
                "type": "Services"
            }
        ],
        "address": "Shop 12 Stavris Complex 465 Stuart Hwy",
        "name": "Intrim",
        "owner": "simplegeo",
        "province": "NT",
        "postcode": "0835",
        "href": "http://api.simplegeo.com/1.0/features/SG_4mgIoipAcMRxxAbUX8b5Ls_-12.523856_131.043243@1303236333.json",
        "country": "AU",
        "city": "Coolalinga"
    },
    "type": "Feature",
    "geometry": {
        "type": "Point",
        "coordinates": [
            131.043243408,
            -12.523856163
        ]
    }
}
	 * @param placeObject
	 * @return
	 * @throws JSONException
	 */
	public Document makePlaceDocument(JSONObject placeObject) throws JSONException, DocumentContentException
	{
				
		JSONObject properties = placeObject.getJSONObject("properties");
		
		Document doc = new Document();
		
		
		doc.add(new Field("_id", placeObject.getString("_id"), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
			
		doc.add(new Field("name", properties.getString("name"), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		
		doc.add(new Field("place", placeObject.toString(), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		
		doc.add(new Field("search", makeSearchableContent(properties), Field.Store.YES,
				Field.Index.ANALYZED));
						
		
		return doc;
		
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
