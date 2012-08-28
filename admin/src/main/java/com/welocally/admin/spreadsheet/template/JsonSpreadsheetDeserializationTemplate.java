package com.welocally.admin.spreadsheet.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.welocally.admin.template.JSONDeserializer;

@Component
public class JsonSpreadsheetDeserializationTemplate {
    
    public List<Entry> entries(JSONObject ofeed) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
        
       
        Object oentry = ofeed.get("entry");
                
        JSONDeserializer<Entry> template = 
            new JSONDeserializer<Entry>(Entry.class);
                
        if(oentry instanceof org.json.JSONObject){
            List<Entry> entry = new ArrayList<Entry>();
            entry.add(template.make((org.json.JSONObject)oentry));
            return entry;
                      
        } else if(oentry instanceof org.json.JSONArray){    
            List<Entry> entries = template.makeResponseAsList((org.json.JSONArray)oentry);            
            return entries;         
        }
                    
        throw new RuntimeException("No Entry Found");
        
    }
    
    public SpreadsheetFeed feed(JSONObject object) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
       
        JSONObject ofeed = object.getJSONObject("feed");
        JSONDeserializer<SpreadsheetFeed> template = 
            new JSONDeserializer<SpreadsheetFeed>(SpreadsheetFeed.class);
        SpreadsheetFeed feed = template.make((org.json.JSONObject)ofeed);        
        feed.setEntry(entries(ofeed));        
        return feed;
              
    }
    
    
    public WorksheetCellFeed cellfeed(JSONObject object) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
       
        JSONObject ofeed = object.getJSONObject("feed");
        JSONDeserializer<WorksheetCellFeed> template = 
            new JSONDeserializer<WorksheetCellFeed>(WorksheetCellFeed.class);
        WorksheetCellFeed feed = template.make((org.json.JSONObject)ofeed);        
        feed.setEntry(cellentries(ofeed));        
        return feed;

              
    }
    
    public List<CellEntry> cellentries(JSONObject ofeed) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
        
       
        Object oentry = ofeed.get("entry");
                
        JSONDeserializer<CellEntry> template = 
            new JSONDeserializer<CellEntry>(CellEntry.class);
                
        if(oentry instanceof org.json.JSONObject){
            List<CellEntry> entry = new ArrayList<CellEntry>();
            entry.add(template.make((org.json.JSONObject)oentry));
            return entry;
                      
        } else if(oentry instanceof org.json.JSONArray){    
            List<CellEntry> entries = template.makeResponseAsList((org.json.JSONArray)oentry);            
            return entries;         
        }
                    
        throw new RuntimeException("No Entry Found");
        
    }
    
    

}
