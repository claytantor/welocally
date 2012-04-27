package com.sightlyinc.ratecred.admin.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Configuration {
    
    @Value("${applicationConfiguration.ajaxServerEndpoint}")
    private String ajaxServerEndpoint;
    
    
    @Value("${applicationConfiguration.iframeTrackerCode:UA-25543611-2}")
    private String iframeTrackerCode;

    public String getAjaxServerEndpoint() {
        return ajaxServerEndpoint;
    }

    public void setAjaxServerEndpoint(String ajaxServerEndpoint) {
        this.ajaxServerEndpoint = ajaxServerEndpoint;
    }

    public String getIframeTrackerCode() {
        return iframeTrackerCode;
    }

    public void setIframeTrackerCode(String iframeTrackerCode) {
        this.iframeTrackerCode = iframeTrackerCode;
    }
    


}
