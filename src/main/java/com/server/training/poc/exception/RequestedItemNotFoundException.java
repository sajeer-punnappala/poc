package com.server.training.poc.exception;

public class RequestedItemNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public RequestedItemNotFoundException(String message) {
		super(message);
	}

}
