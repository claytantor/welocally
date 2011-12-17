package com.sightlyinc.ratecred.admin.model;

/**
 * {
        "type": "Point",
        "coordinates": [
            -122.269882,
            37.808338
        ]
    }
 * @author claygraham
 *
 */
public class Geometry {
	public String type;
	public Double[] coordinates;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Double[] coordinates) {
		this.coordinates = coordinates;
	}
	

}
