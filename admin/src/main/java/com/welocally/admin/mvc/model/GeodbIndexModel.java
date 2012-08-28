package com.welocally.admin.mvc.model;

/*
 * {
    "indexId": "https://spreadsheets.google.com/feeds/cells/0Avb_MOw4lfVndGt6TWYxWFp2bXM0TFlVNi1lejdZQUE/od6/private/full",
    "schema": {
        "properties": [
            {
                "name": "phone",
                "searchable": true
            },
            {
                "name": "address",
                "searchable": true
            },
            {
                "name": "province",
                "searchable": true
            },
            {
                "name": "postcode",
                "searchable": false
            }
        ]
    },
    "records": [
        {
            "_id": "A way to remember",
            "properties": [
                 {
                    "name":"phone",
                    "value":"+1 212 281 5812"
                 },
                "address": "657 Malcolm X Boulevard",
                "province": "NY",
                "postcode": "10037"
            },
            "geometry": {
                "type": "Point",
                "coordinates": [
                    -73.937514,
                    40.819015
                ]
            }
        }
    ]
}
 */
public class GeodbIndexModel {
    private String indexId;
    private GeodbIndexSchema schema;
    private GeodbIndexRecords data;
    
    public String getIndexId() {
        return indexId;
    }
    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }
    public GeodbIndexSchema getSchema() {
        return schema;
    }
    public void setSchema(GeodbIndexSchema schema) {
        this.schema = schema;
    }
    public GeodbIndexRecords getData() {
        return data;
    }
    public void setData(GeodbIndexRecords data) {
        this.data = data;
    }
    

}
