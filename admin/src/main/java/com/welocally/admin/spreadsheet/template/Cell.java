package com.welocally.admin.spreadsheet.template;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * "gs$cell": {
                    "col": "13",
                    "$t": "-73.9827691",
                    "inputValue": "-73.9827691",
                    "numericValue": "-73.9827691",
                    "row": "26"
                }
 * @author claygraham
 *
 */
@JsonIgnoreProperties({"numericValue"})
public class Cell {
    
    @JsonProperty("$t")
    private String value;
    
    private Integer col;
    private String inputValue;
    private Integer row;
    
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCol() {
        return col;
    }
    public void setCol(Integer col) {
        this.col = col;
    }
    public String getInputValue() {
        return inputValue;
    }
    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }
    public Integer getRow() {
        return row;
    }
    public void setRow(Integer row) {
        this.row = row;
    }
    

}
