package com.spring.E_commerce_backend.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {
	
	private String token;
	
	private String message;
}
