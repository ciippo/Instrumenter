package com.instrumentation.exceptions;

public class UnsupportedClassException extends Exception {

	private static final long serialVersionUID = -2060788459810038365L;

	public UnsupportedClassException() {}
	
	public UnsupportedClassException(String message) {
		super(message);
	}
}
