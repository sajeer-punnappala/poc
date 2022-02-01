package com.server.training.poc.model.common;

import java.util.List;


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
public class ErrorVo {
	
	private String errorMessage;
	
	private int erroCode;
	
	private String errorDescription;
	
	private List<FieldError> fieldErrors;
}
