package com.sightlyinc.ratecred.admin.geocoding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.sightlyinc.ratecred.model.Location;

/**
 * probably does not behave as one would like in multi threaded 
 * situations, just be safe with this. 
 * 
 * @author claygraham
 *
 */

@Component("yahooGeocoder")
public class YahooGeocoder implements Geocoder {
	
	@Value("${article.geocodingUrl}")
	private String geocodingUrl="http://where.yahooapis.com/geocode?q=[QUERY]&flags=J&appid=dj0yJmk9RFZyMmFFSk1TdXZWJmQ9WVdrOVVWSjFRbmRJTlRJbWNHbzlOemszTWpnNE9UWXkmcz1jb25zdW1lcnNlY3JldCZ4PTI0";

	@Override
	public Location geocode(String fullAddress) throws GeocoderException {
	
			//now reverse geocode it
	    	Location loc=null;
			try {
				ClientResponse response = 
					SimpleHttpClient.get(geocodingUrl.replace("[QUERY]", fullAddress.replace(" ", "+")), null, null);
				JSONObject jLocation = new JSONObject(new String(response.getResponse()));
				loc = getLocation(jLocation);
			} catch (JSONException e) {
				throw new GeocoderException("problem while trying to geocode:"+fullAddress);
			}
	    	
	    	
	    	if(loc == null)
	    		throw new GeocoderException("location cannot be empty, problem geocoding");
	    	
	    	return loc;
		
	}
	
	  /**
     * {
    "ResultSet": {
        "version": "1.0",
        "Error": 0,
        "ErrorMessage": "No error",
        "Locale": "us_US",
        "Quality": 87,
        "Found": 1,
        "Results": [
            {
                "quality": 85,
                "latitude": "38.898717",
                "longitude": "-77.035974",
                "offsetlat": "38.898590",
                "offsetlon": "-77.035971",
                "radius": 500,
                "name": "",
                "line1": "1600 Pennsylvania Ave NW",
                "line2": "Washington, DC  20006",
                "line3": "",
                "line4": "United States",
                "house": "1600",
                "street": "Pennsylvania Ave NW",
                "xstreet": "",
                "unittype": "",
                "unit": "",
                "postal": "20006",
                "neighborhood": "",
                "city": "Washington",
                "county": "District of Columbia",
                "state": "District of Columbia",
                "country": "United States",
                "countrycode": "US",
                "statecode": "DC",
                "countycode": "DC",
                "uzip": "20006",
                "hash": "B42121631CCA2B89",
                "woeid": 12765843,
                "woetype": 11
            }
        ]
    }
}
     * @param geocodingResponse
     * @return
     * @throws JSONException 
     */
    private Location getLocation(JSONObject geocodingResponse) throws JSONException {
    	Location loc = new Location();
    	JSONObject jResultSet = geocodingResponse.getJSONObject("ResultSet");
    	Integer error = jResultSet.getInt("Error");
    	if(error == 0 && !jResultSet.isNull("Results")) {
    		JSONArray jResults = jResultSet.getJSONArray("Results");
    		if(jResults.length()>0) {
    			JSONObject jfirstLoc = jResults.getJSONObject(0);
    			loc.setAddressOne(jfirstLoc.getString("line1"));
    			loc.setCity(jfirstLoc.getString("city"));
    			loc.setState(jfirstLoc.getString("state"));
    			loc.setPostalCode(jfirstLoc.getString("postal"));
    			loc.setLat(jfirstLoc.getDouble("latitude"));
    			loc.setLng(jfirstLoc.getDouble("longitude"));
    			return loc;
    			
    		} else
    			return null;
    		
    	}
    	return null;
    }



}
