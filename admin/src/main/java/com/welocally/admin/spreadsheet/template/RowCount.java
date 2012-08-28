package com.welocally.admin.spreadsheet.template;

import org.codehaus.jackson.annotate.JsonProperty;

public class RowCount {
    
    @JsonProperty("$t") 
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    

}
