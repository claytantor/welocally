package com.welocally.geodb.services.spatial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class SpatialConversionUtils {
	
	public Point getJSONPoint(JSONObject placeObject){
		try {
			JSONObject geom = placeObject.getJSONObject("geometry");
			JSONArray coords = geom.getJSONArray("coordinates");
			Point coord = new Point(
					Double.parseDouble(coords.getString(1)), Double
							.parseDouble(coords.getString(0)));
			return coord;
		} catch (NumberFormatException e) {
			return null;
		} catch (JSONException e) {
			return null;
		}
	}

}
