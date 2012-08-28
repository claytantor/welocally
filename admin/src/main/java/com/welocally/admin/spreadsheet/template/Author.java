package com.welocally.admin.spreadsheet.template;


/*
 * "author": [
            {
                "name": {
                    "$t": "clay"
                },
                "email": {
                    "$t": "clay@welocally.com"
                }
            }
        ],
 */
public class Author {
    
    private StringValue name;
    private StringValue email;
    public StringValue getName() {
        return name;
    }
    public void setName(StringValue name) {
        this.name = name;
    }
    public StringValue getEmail() {
        return email;
    }
    public void setEmail(StringValue email) {
        this.email = email;
    }

}
