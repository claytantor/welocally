package com.welocally.admin.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDeserializer<T> {
    
    private final Class<T> type;
    
    private ObjectMapper mapper = new ObjectMapper();
    
    static Logger logger = 
        Logger.getLogger(JSONDeserializer.class);
    
    
    public JSONDeserializer(final Class<T> type) {
        super();
        this.type = type;       
    }

    public List<T> makeResponseAsList(JSONArray response) 
        throws JsonParseException, JsonMappingException, IOException, JSONException
    {
        List<T> all = new ArrayList<T>();
        for (int i = 0; i < response.length(); i++) {
            all.add(make(response.getJSONObject(i)));
        }       
        return all;
    }
    
    public T make(JSONObject response) 
        throws JsonParseException, JsonMappingException, IOException 
    {
        T result = (T)mapper.readValue(
                response.toString(), type);                 
        return result;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
    
}
