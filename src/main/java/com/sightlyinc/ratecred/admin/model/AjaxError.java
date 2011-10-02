package com.sightlyinc.ratecred.admin.model;

public class AjaxError {
	private Integer errorCode;
	private String errorMessage;
	
	
	public AjaxError(Integer errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}	
}
