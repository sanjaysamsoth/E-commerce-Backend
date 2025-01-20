package com.spring.E_commerce_backend.exception;

public class SellerNotFoundException extends RuntimeException{
	
	public SellerNotFoundException() {
		super();
	}

	public SellerNotFoundException(String message) {
		super(message);
	}
}
