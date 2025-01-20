package com.spring.E_commerce_backend.controller;

public class ProductNotFound extends RuntimeException{

	public ProductNotFound() {
	}

	public ProductNotFound(String message) {
		super(message);
	}

}
