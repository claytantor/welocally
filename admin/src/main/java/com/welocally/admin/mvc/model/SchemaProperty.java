package com.welocally.admin.mvc.model;

public class SchemaProperty {
    private String name;
    private Boolean searchable;
     
    public SchemaProperty() {
        super();
    }

    public SchemaProperty(String name, Boolean searchable) {
        super();
        this.name = name;
        this.searchable = searchable;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getSearchable() {
        return searchable;
    }
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }
    
    

}
