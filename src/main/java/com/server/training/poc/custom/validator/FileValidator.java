package com.server.training.poc.custom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.server.training.poc.common.PocConstants;
import com.server.training.poc.custom.annotation.ValidFile;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile>{

	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		boolean isValid = true;
		if(multipartFile.getContentType() == null) {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "File is mandatory")
                   .addConstraintViolation();
            isValid = false;
		} else if(!multipartFile.getContentType().equalsIgnoreCase(PocConstants.MIME_TYPE_TXT)) {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Only .txt files are allowed.")
                   .addConstraintViolation();
            isValid = false;
		}
		return isValid;
	}

}
