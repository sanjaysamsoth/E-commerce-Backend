package com.spring.E_commerce_backend.controller;

import com.spring.E_commerce_backend.models.Cart;
import com.spring.E_commerce_backend.models.CartDTO;
import com.spring.E_commerce_backend.repository.CartDao;
import com.spring.E_commerce_backend.repository.CustomerDao;
import com.spring.E_commerce_backend.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CartController {

	private final CartService cartService;

	private final CartDao cartDao;

	private final CustomerDao customerDao;

	public CartController(CartService cartService, CartDao cartDao, CustomerDao customerDao) {
		this.cartService = cartService;
		this.cartDao = cartDao;
		this.customerDao = customerDao;
	}

	@PostMapping(value = "/cart/add")
	public ResponseEntity<Cart> addProductToCartHander(@RequestBody CartDTO cartdto , @RequestHeader("token")String token){
		
		Cart cart = cartService.addProductToCart(cartdto, token);
		return new ResponseEntity<>(cart,HttpStatus.CREATED);
	}

	@GetMapping(value = "/cart")
	public ResponseEntity<Cart> getCartProductHandler(@RequestHeader("token")String token){
		return new ResponseEntity<>(cartService.getCartProduct(token), HttpStatus.ACCEPTED);
	}

	@DeleteMapping(value = "/cart")
	public ResponseEntity<Cart> removeProductFromCartHander(@RequestBody CartDTO cartdto ,@RequestHeader("token")String token){
		
		Cart cart = cartService.removeProductFromCart(cartdto, token);
		return new ResponseEntity<>(cart,HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/cart/clear")
	public ResponseEntity<Cart> clearCartHandler(@RequestHeader("token") String token){
		return new ResponseEntity<>(cartService.clearCart(token), HttpStatus.ACCEPTED);
	}
}
