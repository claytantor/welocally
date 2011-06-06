package com.sightlyinc.ratecred.admin.mvc.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sightlyinc.ratecred.admin.model.NetworkMemberForm;

@Component("networkMemberValidator")
public class NetworkMemberValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return NetworkMemberForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "type", "type.empty");
		if(!((NetworkMemberForm)target).getType().equalsIgnoreCase("PUBLISHER"))
			errors.rejectValue("type", "type.unsupported");
		
	}

}
