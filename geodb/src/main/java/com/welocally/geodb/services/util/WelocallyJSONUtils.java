package com.welocally.geodb.services.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;


@Component
public class WelocallyJSONUtils {
	
	public void updatePlaceToWelocally(JSONObject place) throws JSONException{
		place.put("_id", place.getString("id").replaceAll("SG_", "WL_"));
		place.put("type", "Place");
		place.remove("id");
		JSONObject properties = place.getJSONObject("properties");
		properties.remove("href");
		properties.put("owner", "welocally");
	}

}
