package com.spring.E_commerce_backend.service;

import com.spring.E_commerce_backend.controller.ProductNotFound;
import com.spring.E_commerce_backend.exception.CartItemNotFound;
import com.spring.E_commerce_backend.models.Cart;
import com.spring.E_commerce_backend.models.CartDTO;
import com.spring.E_commerce_backend.models.Customer;
import com.spring.E_commerce_backend.models.Product;

import java.util.List;


public interface CartService {
	
	public Cart addProductToCart(CartDTO cart, String token) throws CartItemNotFound;
	public Cart getCartProduct(String token);
	public Cart removeProductFromCart(CartDTO cartDto,String token) throws ProductNotFound;

	//public Cart changeQuantity(Product product, Customer customer, Integer quantity);
	
	public Cart clearCart(String token);
	
}
