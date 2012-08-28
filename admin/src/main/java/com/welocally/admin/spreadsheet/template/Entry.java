package com.welocally.admin.spreadsheet.template;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Entry {
    private StringValue id;
    
    private Content content;
    
    @JsonProperty("gs$rowCount") 
    private RowCount rowCount;
    
    @JsonProperty("gs$colCount") 
    private ColumnCount columnCount;
    
    private Title title;
    
    private List<Category> category;
    
    private StringValue updated;
    
    private List<Link> link;
    

    public StringValue getId() {
        return id;
    }

    public void setId(StringValue id) {
        this.id = id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
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

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public StringValue getUpdated() {
        return updated;
    }

    public void setUpdated(StringValue updated) {
        this.updated = updated;
    }
    
    
    
}
