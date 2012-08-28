package com.welocally.admin.spreadsheet.template;

import org.codehaus.jackson.annotate.JsonProperty;

public class Title {
    
    @JsonProperty("$t") 
    private String name;
    
    private String type;
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
