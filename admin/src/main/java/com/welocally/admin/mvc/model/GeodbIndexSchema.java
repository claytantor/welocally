package com.welocally.admin.mvc.model;

import java.util.ArrayList;
import java.util.List;

public class GeodbIndexSchema {
    
    private SchemaProperty idField;
    
    private List<SchemaProperty> properties;
    
    public GeodbIndexSchema() {
        super();
        properties = new ArrayList<SchemaProperty>();
    }

    public List<SchemaProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<SchemaProperty> properties) {
        this.properties = properties;
    }

    public SchemaProperty getIdField() {
        return idField;
    }

    public void setIdField(SchemaProperty idField) {
        this.idField = idField;
    }
    

}
