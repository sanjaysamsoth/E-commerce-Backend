package com.spring.E_commerce_backend.service;

import com.spring.E_commerce_backend.models.CartDTO;
import com.spring.E_commerce_backend.models.CartItem;

public interface CartItemService {
	
	public CartItem createItemforCart(CartDTO cartdto);
	
}
