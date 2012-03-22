package com.sightlyinc.ratecred.admin.model;

import java.util.Set;

public interface Errors {

    public abstract Set<AjaxError> getErrors();

    public abstract void setErrors(Set<AjaxError> errors);

}