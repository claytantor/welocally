package com.welocally.admin.mvc.model;

/**
 * "geometry": {
                "type": "Point",
                "coordinates": [
                    -73.937514,
                    40.819015
                ]
            }
 * @author claygraham
 *
 */
public class GeodbGeometry {
    private String type;
    private Double[] coordinates;
    
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
