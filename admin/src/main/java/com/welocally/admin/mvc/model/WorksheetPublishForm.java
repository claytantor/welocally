package com.welocally.admin.mvc.model;

public class WorksheetPublishForm {
    
    /*<input type="hidden" val="${worksheet.name}" name="name">
    <input type="hidden" val="${worksheet.id}" name="feedUrl">
    primary key field:<input type="text" name="primaryKey">
    search fields:<input type="text" name="searchFields">
    lat:<input type="text" name="lat">
    lng:<input type="text" name="lng">*/
    
    private String feedUrl;
    private String name;
    private String primaryKey;
    private String searchFields;
    private String lat;
    private String lng;
      

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(String searchFields) {
        this.searchFields = searchFields;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

   
    
}
