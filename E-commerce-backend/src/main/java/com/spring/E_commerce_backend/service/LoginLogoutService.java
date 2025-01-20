package com.spring.E_commerce_backend.service;

import com.spring.E_commerce_backend.models.CustomerDTO;
import com.spring.E_commerce_backend.models.SellerDTO;
import com.spring.E_commerce_backend.models.SessionDTO;
import com.spring.E_commerce_backend.models.UserSession;

public interface LoginLogoutService {
	
	public UserSession loginCustomer(CustomerDTO customer);
	
	public SessionDTO logoutCustomer(SessionDTO session);
	
	public void checkTokenStatus(String token);
	
	public void deleteExpiredTokens();

	public UserSession loginSeller(SellerDTO seller);
	
	public SessionDTO logoutSeller(SessionDTO session);

}
