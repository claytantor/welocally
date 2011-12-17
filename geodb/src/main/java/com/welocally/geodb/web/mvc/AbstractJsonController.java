package com.welocally.geodb.web.mvc;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.welocally.geodb.services.component.JsonObjectSerializer;
import com.welocally.geodb.services.model.AjaxError;
import com.welocally.geodb.services.model.AjaxErrors;

public abstract class AbstractJsonController {
	
	@Autowired
	JsonObjectSerializer jsonObjectSerializer;
	
	protected String makeErrorsJson(Exception e){
		try {
			return jsonObjectSerializer.serialize(makeErrors(e));
		} catch (IOException e1) {
			return "[{\"errorMessage\":\"server error\",\"errorCode\":106 }]";
		}
	}
	
	protected AjaxErrors makeErrors(Exception e){
		AjaxErrors errors = new AjaxErrors();
		AjaxError error = new AjaxError(AjaxError.WL_SERVER_ERROR,e.getMessage());
		errors.getErrors().add(error);
		return errors;
	}

}
