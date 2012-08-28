package com.welocally.admin.spreadsheet.template;

import org.codehaus.jackson.annotate.JsonProperty;

public class CellEntry extends Entry {
    
    @JsonProperty("gs$cell")
    private Cell cell;

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
    

}
