package com.welocally.admin.spreadsheet.template;

import org.codehaus.jackson.annotate.JsonProperty;

public class StringValue {
    @JsonProperty("$t") 
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
