package com.spring.E_commerce_backend.exception;

public class ProductNotFoundException extends RuntimeException{

	public ProductNotFoundException() {
	}
	
	public ProductNotFoundException(String message){
		super(message);
	}
}
