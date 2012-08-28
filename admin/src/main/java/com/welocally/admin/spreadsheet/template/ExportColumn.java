package com.welocally.admin.spreadsheet.template;

public class ExportColumn {
    
    private Integer colnum;
    private Cell cell;
    private String text;
    private String title;
    
    
    public Integer getColnum() {
        return colnum;
    }
    public void setColnum(Integer colnum) {
        this.colnum = colnum;
    }
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public Cell getCell() {
        return cell;
    }
    public void setCell(Cell cell) {
        this.cell = cell;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }   

}
