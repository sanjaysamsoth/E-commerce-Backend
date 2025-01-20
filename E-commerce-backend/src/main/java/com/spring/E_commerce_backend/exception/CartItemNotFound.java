package com.spring.E_commerce_backend.exception;

public class CartItemNotFound extends RuntimeException{

	public CartItemNotFound() {
	}
	
	public CartItemNotFound(String message) {
		super(message);
	}
}
