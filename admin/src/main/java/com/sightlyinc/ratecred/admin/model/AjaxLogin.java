package com.sightlyinc.ratecred.admin.model;

public class AjaxLogin {
	public String status;
	public String url;
		
	public AjaxLogin(String status, String url) {
		super();
		this.status = status;
		this.url = url;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
