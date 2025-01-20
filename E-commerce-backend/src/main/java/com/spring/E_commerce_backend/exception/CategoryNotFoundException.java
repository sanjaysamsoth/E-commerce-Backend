package com.spring.E_commerce_backend.exception;

public class CategoryNotFoundException extends RuntimeException{

	public CategoryNotFoundException() {
	}

	public CategoryNotFoundException(String message) {
		super(message);
	}
	
}
