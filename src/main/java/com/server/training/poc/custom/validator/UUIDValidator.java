package com.server.training.poc.custom.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.ObjectUtils;

import com.server.training.poc.custom.annotation.ValidUuid;

public class UUIDValidator implements ConstraintValidator<ValidUuid, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Pattern pattern = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
		if(ObjectUtils.isEmpty(value)) {
			return false;
		}
		return pattern.matcher(value).matches();
	}
	

}
