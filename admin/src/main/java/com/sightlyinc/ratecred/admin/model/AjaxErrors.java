package com.sightlyinc.ratecred.admin.model;

import java.util.HashSet;
import java.util.Set;

public class AjaxErrors implements Errors {
	Set<AjaxError> errors = new HashSet<AjaxError>();

	/* (non-Javadoc)
     * @see com.sightlyinc.ratecred.admin.model.Errors#getErrors()
     */
	public Set<AjaxError> getErrors() {
		return errors;
	}

	/* (non-Javadoc)
     * @see com.sightlyinc.ratecred.admin.model.Errors#setErrors(java.util.Set)
     */
	public void setErrors(Set<AjaxError> errors) {
		this.errors = errors;
	}

	
	
	

}
