package com.sightlyinc.ratecred.admin.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Configuration {
    
    @Value("${applicationConfiguration.ajaxServerEndpoint}")
    private String ajaxServerEndpoint;

    public String getAjaxServerEndpoint() {
        return ajaxServerEndpoint;
    }

    public void setAjaxServerEndpoint(String ajaxServerEndpoint) {
        this.ajaxServerEndpoint = ajaxServerEndpoint;
    }
    
    

}
