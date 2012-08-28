package com.sightlyinc.ratecred.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PublisherManager {
    
//    public Map<String,String> isAllowed(Map<String,String> data, Publisher p, UserPrincipal up){   
//        
//        Map<String,String> resultModel = new HashMap<String,String>();
//        if(data.get("command").equals("savePlace")){                       
//            if(!hasAnyRole(up, new String[] {"WL_PLACES_PRO","WL_PLACES_PREMIUM"}) 
//                    && Integer.parseInt(data.get("places"))>20){
//                resultModel.put("allowed", "false");
//            } else {
//                resultModel.put("allowed", "true");
//            }
//        }
//        return resultModel;
//    }
//    
//    public boolean hasAnyRole(UserPrincipal up, String[] roles){
//
//        GrantedAuthority[] auths = up.getAuthorities();
//        for (int i = 0; i < auths.length; i++) {
//            for (int j = 0; j < roles.length; j++) {
//                if(auths[i].getAuthority().equals(roles[j]))
//                    return true;
//            }
//        }
//        
//        return false;
//    }

}
