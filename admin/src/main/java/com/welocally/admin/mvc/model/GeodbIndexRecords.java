package com.welocally.admin.mvc.model;

import java.util.ArrayList;
import java.util.List;

public class GeodbIndexRecords {
    
    private List<GeodbIndexRecord> records;
    
    

    public GeodbIndexRecords() {
        super();
        records = new ArrayList<GeodbIndexRecord>();
    }

    public List<GeodbIndexRecord> getRecords() {
        return records;
    }

    public void setRecords(List<GeodbIndexRecord> records) {
        this.records = records;
    }
    

}
