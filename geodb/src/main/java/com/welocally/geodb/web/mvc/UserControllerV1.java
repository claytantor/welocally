package com.welocally.geodb.web.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.IdGen;
import com.welocally.geodb.services.db.JsonDatabase;
import com.welocally.geodb.services.spatial.SpatialConversionUtils;
import com.welocally.geodb.services.util.WelocallyJSONUtils;

@Controller
@RequestMapping("/user/1_0")
public class UserControllerV1 extends AbstractJsonController {
    
    static Logger logger = 
        Logger.getLogger(ESPlaceController.class);
        
    @Autowired 
    @Qualifier("dynamoJsonDatabase")
    JsonDatabase jsonDatabase;
    
    
    @Value("${placesDatabase.collectionName:dev.places.published}")
    String placesCollection;
    
    @Value("${placesDatabase.collectionName:dev.places.review}")
    String placesReviewCollection;
            
    @Value("${publisherDatabase.collectionName:dev.publishers}")
    String publisherCollection;
    
    @Value("${userDatabase.collectionName:dev.user.places}")
    String publisherPlacesCollection;
    
    @Value("${ElasticSearch.transportClient.server:localhost}")
    String elasticSearchTransportServer;
    
    @Value("${ElasticSearch.transportClient.port:9300}")
    Integer elasticSearchTransportPort;
    
    @Value("${ElasticSearch.transportClient.clusterName:es-welocally-dev}")
    String elasticSearchTransportClusterName;
    
    @Value("${geodb.admin.username}")
    String adminUser;
    
    @Value("${geodb.admin.password}")
    String adminPassword;
    
        
    @Autowired IdGen idGen; 
    
    @Autowired SpatialConversionUtils spatialConversionUtils; 
    
    @Autowired WelocallyJSONUtils welocallyJSONUtils;
    
    TransportClient transportClient = null;
    
    
    @PostConstruct
    public void initClient(){
        Settings settings = ImmutableSettings.settingsBuilder()
           .put("cluster.name", elasticSearchTransportClusterName).build();
       transportClient = new TransportClient(settings);
       transportClient.addTransportAddress(
               new InetSocketTransportAddress(
                       elasticSearchTransportServer, 
                       elasticSearchTransportPort));
    }
    
      
//  curl -XPOST localhost:9200/user52ae/place/_mapping -d '{
//  "place": {
//      "properties": {
//          "location": {
//              "type": "geo_point"
//          },
//          "search": {
//              "type": "string",
//              "store": "yes"
//          }
//      }
//  }
//  }'
    
    @RequestMapping(value = "/user/create", method = RequestMethod.PUT)
    public ModelAndView createUser(@RequestBody String requestJson, HttpServletRequest req) {
        
        ModelAndView  mav = new ModelAndView("mapper-result");
      
        try {
            
            //right now we are going to secure this with the claytantor account
            JSONObject  publisher =  checkPublisherKey(req); 
            
            if(publisher.getString("username").equals(adminUser) && 
                    publisher.getString("password").equals(adminPassword) )
            {
                JSONObject user = 
                    new JSONObject(requestJson);
                
                //put it in the user store
                jsonDatabase.put(user, publisherCollection, user.getString("username"), JsonDatabase.EntityType.PUBLISHER);
                
                String mapping = "{" +
                    "\"place\": {" +
                        "\"properties\":{" +
                            "\"location\":{ " +
                                "\"type\": \"geo_point\"" +
                              "}," +
                              "\"search\": {" +
                                 "\"type\": \"string\"," +
                                 "\"store\": \"yes\"" +
                                "}" +
                            "}" +
                        "}" +
                        "}";     
                
                CreateIndexResponse response = 
                    transportClient.admin().indices().create( 
                                        new CreateIndexRequest(user.getString("username")). 
                                                mapping("place", mapping) 
                                ).actionGet(); 
                    
                Map<String, Object> result = new HashMap<String,Object>();
                result.put("acknowledged", response.acknowledged());
                result.put("status", "SUCCEED");
                
                mav.addObject("mapperResult", makeModelJson(result));
                 
            } else {
                throw new RuntimeException("user not authorized for this activity");
            }
                       
        } catch (JSONException e) {
            logger.error("could not get results");
            mav.addObject("mapperResult", makeErrorsJson(e));
        } catch (RuntimeException e) {
            logger.error("could not get results");
            mav.addObject("mapperResult", makeErrorsJson(e));
        } catch (DbException e) {
            logger.error("could not get results");
            mav.addObject("mapperResult", makeErrorsJson(e));
        } 
        
        return mav;
   
    }

     
    private JSONObject checkPublisherKey(HttpServletRequest req){
        if(req.getHeader("key") != null){   
            String publisherKey = req.getHeader("key");
                      
            try {
                return jsonDatabase.findById(publisherCollection, publisherKey);
            } catch (Exception e) {
               throw new RuntimeException(e);
            }  
                                       
        } else {  
            throw new RuntimeException("publisher key not found in header");
        }
    }
 
    public void setJsonDatabase(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public void setIdGen(IdGen idGen) {
        this.idGen = idGen;
    }

    public void setSpatialConversionUtils(
            SpatialConversionUtils spatialConversionUtils) {
        this.spatialConversionUtils = spatialConversionUtils;
    }
    
    
}