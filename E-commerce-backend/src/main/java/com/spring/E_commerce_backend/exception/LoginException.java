package com.spring.E_commerce_backend.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginException extends RuntimeException{

	public LoginException(String message) {
		super(message);
	}
	
}
