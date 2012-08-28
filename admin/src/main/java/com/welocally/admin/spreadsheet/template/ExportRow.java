package com.welocally.admin.spreadsheet.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExportRow {
    private Integer rownum;
    private Map<Integer,ExportColumn> columns;
    
    public ExportRow() {
        super();
        columns = new TreeMap<Integer,ExportColumn>();
    }
    
    public Integer getRownum() {
        return rownum;
    }
    public void setRownum(Integer rownum) {
        this.rownum = rownum;
    }
    
    public void putCell(Integer colnum, String title, Cell c){
        ExportColumn col = new ExportColumn();
        col.setCell(c);
        col.setColnum(colnum);
        col.setText(c.getValue());
        col.setTitle(title);
        columns.put(colnum, col);       
    }
    
    public List<ExportColumn> getColumns() {
        return new ArrayList<ExportColumn>(columns.values());
    }
}
