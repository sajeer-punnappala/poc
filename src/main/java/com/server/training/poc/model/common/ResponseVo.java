package com.server.training.poc.model.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVo<T> {
	
	private HttpStatus httpStatus;
	
	private int statusCode;
	
	private T data;
	
	private ErrorVo errorVo;
	
	private String message;
}
