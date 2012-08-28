package com.welocally.admin.spreadsheet.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorksheetGrid {
    
    private String name;
    private String id;
    
    private List<ExportRow> rows;
    private Map<Integer,String> colNames;
    

    public WorksheetGrid() {
        super();
        colNames = new HashMap<Integer,String>();
    }

    public List<ExportRow> getRows() {
        return rows;
    }

    public void setRows(List<ExportRow> rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Integer, String> getColNames() {
        return colNames;
    }

    public void setColNames(Map<Integer, String> colNames) {
        this.colNames = colNames;
    }


    
}
