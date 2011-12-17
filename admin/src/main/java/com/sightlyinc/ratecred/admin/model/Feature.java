package com.sightlyinc.ratecred.admin.model;

import java.util.Map;



/**
 * {
    "geometry": {
        "type": "Point",
        "coordinates": [
            -122.269882,
            37.808338
        ]
    },
    "type": "Feature",
    "id": "SG_5PbkiDEAgs9dxmOlRSH1Oy_37.808338_-122.269882@1303263346",
    "properties": {
        "province": "CA",
        "distance": 0.3748500422932057,
        "name": "Fox Theatre",
        "tags": [
            "theatre"
        ],
        "country": "US",
        "phone": "+1 510 835 0887",
        "href": "http://api.simplegeo.com/1.0/features/SG_5PbkiDEAgs9dxmOlRSH1Oy_37.808338_-122.269882@1303263346.json",
        "city": "Oakland",
        "address": "1912 Telegraph Ave",
        "owner": "simplegeo",
        "postcode": "94612",
        "classifiers": [
            {
                "category": "Arts & Performance",
                "type": "Entertainment",
                "subcategory": "Theater"
            }
        ]
    }
}
 * @author claygraham
 *
 */
public class Feature {
	
	private String id;
	private Geometry geometry;
	private String type;
	//private FeatureProperties properties;
	private Map<String, Object> properties;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
//	public FeatureProperties getProperties() {
//		return properties;
//	}
//	public void setProperties(FeatureProperties properties) {
//		this.properties = properties;
//	}
//	
	public Map<String, Object> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}


}
