package com.welocally.admin.mvc.model;

import java.util.List;

public class GeodbIndexRecord {
    
    private String id;
    
    private List<NameValue> properties;
    
    private GeodbGeometry geometry;

    public List<NameValue> getProperties() {
        return properties;
    }

    public void setProperties(List<NameValue> properties) {
        this.properties = properties;
    }

    

    public GeodbGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GeodbGeometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    

}
