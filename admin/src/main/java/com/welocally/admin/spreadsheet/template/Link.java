package com.welocally.admin.spreadsheet.template;

/*
 *  {
        "rel": "http://schemas.google.com/spreadsheets/2006#listfeed",
        "type": "application/atom+xml",
        "href": "https://spreadsheets.google.com/feeds/list/0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE/od6/private/full"
    }
 */
public class Link {
    
    private String rel;
    private String type;
    private String href;
    public String getRel() {
        return rel;
    }
    public void setRel(String rel) {
        this.rel = rel;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }

}
