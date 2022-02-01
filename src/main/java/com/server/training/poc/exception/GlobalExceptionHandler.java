package com.server.training.poc.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.server.training.poc.model.common.ErrorVo;
import com.server.training.poc.model.common.FieldError;
import com.server.training.poc.model.common.ResponseVo;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ResponseVo<Object>> handleGenericException(Exception ex, WebRequest request) throws Exception {
		LOGGER.error("Exception : ",ex);
		ErrorVo errorVo = new ErrorVo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),null);
		ResponseVo<Object> responseVo = new ResponseVo<Object>(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), null, errorVo, null);
		return new ResponseEntity<ResponseVo<Object>>(responseVo,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RequestedItemNotFoundException.class)
	public final ResponseEntity<ResponseVo<Object>> handleStudentNotFoundException(RequestedItemNotFoundException ex, WebRequest request) {
		ErrorVo errorVo = new ErrorVo(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value(), ex.getMessage(),null);
		ResponseVo<Object> responseVo = new ResponseVo<Object>(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), null, errorVo,null);
		return new ResponseEntity<ResponseVo<Object>>(responseVo,HttpStatus.NOT_FOUND);
	}
	
	@Override
	public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println(ex.getBindingResult().getAllErrors());
		System.out.println(ex.getFieldErrors());
		ErrorVo errorVo = new ErrorVo(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), null, formatBindingResults(ex.getBindingResult()));
		ResponseVo<Object> responseVo = new ResponseVo<Object>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), null, errorVo, null);
		return new ResponseEntity(responseVo,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ResponseVo<Object>> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) throws Exception {
		ErrorVo errorVo = new ErrorVo(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(),null);
		ResponseVo<Object> responseVo = new ResponseVo<Object>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), null, errorVo, null);
		return new ResponseEntity<ResponseVo<Object>>(responseVo,HttpStatus.BAD_REQUEST);
	}
	
	
	@Override
	public final ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorVo errorVo = new ErrorVo(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(),null, formatBindingResults(ex.getBindingResult()));
		ResponseVo<Object> responseVo = new ResponseVo<Object>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), null, errorVo, null);
		return new ResponseEntity(responseVo,HttpStatus.BAD_REQUEST);
	}
	public List<FieldError> formatBindingResults(BindingResult result){
		List<FieldError> fieldErrors = new ArrayList<>();
		result.getFieldErrors().forEach(fe -> {
			FieldError fieldErrorObj = new FieldError();
			fieldErrorObj.setFieldName(fe.getField());
			fieldErrorObj.setErrorMessage(fe.getDefaultMessage());
			fieldErrors.add(fieldErrorObj);
		});
		return fieldErrors;
	}
	 
	
	 
}
