package com.welocally.geodb.services.spatial;

import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.spatial.tier.projections.IProjector;
import org.apache.lucene.spatial.tier.projections.SinusoidalProjector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class SpatialConversionUtils {
	
	public static final String LAT_FIELD = "lat";
	public static final String LON_FIELD = "lng";
	
	public static final double MILE = 1.609344;
  	
	private double maxMiles = 250, minMiles = 1;
	private IProjector projector = new SinusoidalProjector();
	private CartesianTierPlotter ctp = new CartesianTierPlotter(0, projector, CartesianTierPlotter.DEFALT_FIELD_PREFIX);
	// startTier is 14 for 25 miles, 15 for 1 miles in lucene 3.0
	private int startTier = ctp.bestFit(maxMiles), endTier = ctp.bestFit(minMiles);
	
	
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


	public double getMaxMiles() {
		return maxMiles;
	}


	public void setMaxMiles(double maxMiles) {
		this.maxMiles = maxMiles;
	}


	public double getMinMiles() {
		return minMiles;
	}


	public void setMinMiles(double minMiles) {
		this.minMiles = minMiles;
	}


	public IProjector getProjector() {
		return projector;
	}


	public void setProjector(IProjector projector) {
		this.projector = projector;
	}


	public CartesianTierPlotter getCtp() {
		return ctp;
	}


	public void setCtp(CartesianTierPlotter ctp) {
		this.ctp = ctp;
	}


	public int getStartTier() {
		return startTier;
	}


	public void setStartTier(int startTier) {
		this.startTier = startTier;
	}


	public int getEndTier() {
		return endTier;
	}


	public void setEndTier(int endTier) {
		this.endTier = endTier;
	}
	
	

}
