package com.welocally.admin.spreadsheet.template;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties({"openSearch$startIndex","xmlns$batch","xmlns$gs","openSearch$totalResults","xmlns","xmlns$openSearch"})
public class WorksheetCellFeed extends Feed {
    
    @JsonIgnore
    List<CellEntry> entry;
    
    @JsonProperty("gs$rowCount") 
    private RowCount rowCount;
    
    @JsonProperty("gs$colCount") 
    private ColumnCount columnCount;

    public List<CellEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<CellEntry> entry) {
        this.entry = entry;
    }

    public RowCount getRowCount() {
        return rowCount;
    }

    public void setRowCount(RowCount rowCount) {
        this.rowCount = rowCount;
    }

    public ColumnCount getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(ColumnCount columnCount) {
        this.columnCount = columnCount;
    }
    
    

}
