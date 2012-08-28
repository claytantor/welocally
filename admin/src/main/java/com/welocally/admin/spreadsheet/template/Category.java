package com.welocally.admin.spreadsheet.template;

/*
 * "category": [
                    {
                        "scheme": "http://schemas.google.com/spreadsheets/2006",
                        "term": "http://schemas.google.com/spreadsheets/2006#worksheet"
                    }
                ],
 */
public class Category {
    
    private String scheme;
    private String term;
    public String getScheme() {
        return scheme;
    }
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }
    

}
