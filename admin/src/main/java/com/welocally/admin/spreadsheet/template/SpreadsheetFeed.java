package com.welocally.admin.spreadsheet.template;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class SpreadsheetFeed extends Feed {
    
        @JsonIgnore
        List<Entry> entry;

        public List<Entry> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry> entry) {
            this.entry = entry;
        }
        
           
}
