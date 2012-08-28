package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.admin.util.HttpHelperUtils;
import com.sightlyinc.ratecred.admin.util.JsonObjectSerializer;
import com.welocally.admin.security.UserPrincipal;

@Component
public class GeodbProvisionManager {
    
    public enum GeodbProvisionStatus {PROVISIONED, EMPTY}
    
//    public enum MyType {
//        PROVISIONED {
//            public String toString() {
//                return "PROVISIONED";
//            }
//        },
//         
//        EMPTY {
//            public String toString() {
//                return "EMPTY";
//            }
//        }
//    }
      

    @Autowired
    private JsonObjectSerializer jsonObjectSerializer;
    
    @Autowired
    private HttpHelperUtils httpHelperUtils;
    
    @Value("${geodb.endpoint:http://gaudi.welocally.com/geodb}")
    private String geodbEnpoint;
    
    @Value("${geodb.provisionRequestMapping:/user/1_0/create.json}")
    private String provisionRequestMapping;
    
    @Value("${geodb.provisionExistsRequestMapping:/user/1_0/exists}")
    private String provisionStstusExistsRequestMapping;
    
    public void provision(UserPrincipal up, String adminUsername, String adminPassword) throws JSONException, NoSuchAlgorithmException, IOException{
        //provision
        JSONObject upObject = 
            new JSONObject(jsonObjectSerializer.serialize(up));
        
        String url = geodbEnpoint+provisionRequestMapping;
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        String wireToken = new String(md.digest(adminPassword.getBytes()));
        httpHelperUtils.put(upObject, adminUsername, wireToken, url);

    }
    
    public GeodbProvisionStatus status(UserPrincipal up, String adminUsername, String adminPassword) throws NoSuchAlgorithmException, IOException, JSONException {
        String url = geodbEnpoint+provisionStstusExistsRequestMapping+"/"+up.getUsername()+".json";
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        String wireToken = new String(md.digest(up.getPassword().getBytes()));                
        JSONObject provisionStatus = httpHelperUtils.get(url, adminUsername, wireToken);
        if(provisionStatus.isNull("errors") && !provisionStatus.isNull("id")){
            return GeodbProvisionStatus.PROVISIONED;
        } else {
            return GeodbProvisionStatus.EMPTY;
        }

    }

}
