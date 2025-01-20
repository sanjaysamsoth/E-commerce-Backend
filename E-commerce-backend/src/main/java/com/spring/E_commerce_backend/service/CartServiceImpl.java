package com.spring.E_commerce_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.spring.E_commerce_backend.exception.CartItemNotFound;
import com.spring.E_commerce_backend.exception.CustomerNotFoundException;
import com.spring.E_commerce_backend.exception.LoginException;
import com.spring.E_commerce_backend.models.*;
import com.spring.E_commerce_backend.repository.CartDao;
import com.spring.E_commerce_backend.repository.CustomerDao;
import com.spring.E_commerce_backend.repository.ProductDao;
import com.spring.E_commerce_backend.repository.SessionDao;
import org.springframework.stereotype.Service;


@Service
public class CartServiceImpl implements CartService {

	private final CartDao cartDao;
	private final SessionDao sessionDao;
	private final CartItemService cartItemService;
	private final CustomerDao customerDao;
	private final LoginLogoutService loginService;
	private final ProductDao productDao;

	public CartServiceImpl(CartDao cartDao, SessionDao sessionDao, CartItemService cartItemService,
						   CustomerDao customerDao, LoginLogoutService loginService, ProductDao productDao) {
		this.cartDao = cartDao;
		this.sessionDao = sessionDao;
		this.cartItemService = cartItemService;
		this.customerDao = customerDao;
		this.loginService = loginService;
		this.productDao = productDao;
	}

	@Override
	public Cart addProductToCart(CartDTO cartDto, String token) {


		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		Cart customerCart = existingCustomer.getCustomerCart();
		
		List<CartItem> cartItems = customerCart.getCartItems();
		
		CartItem item = cartItemService.createItemforCart(cartDto);


		if(cartItems.isEmpty()) {
			cartItems.add(item);
			customerCart.setCartTotal(item.getCartProduct().getPrice());
		}
		else {
			boolean flag = false;
			for(CartItem c: cartItems) {
				if(c.getCartProduct().getProductId() == cartDto.getProductId()) {
					c.setCartItemQuantity(c.getCartItemQuantity() + 1);
					customerCart.setCartTotal(customerCart.getCartTotal() + c.getCartProduct().getPrice());
					flag = true;
				}
			}
			if(!flag) {
				cartItems.add(item);
				customerCart.setCartTotal(customerCart.getCartTotal() + item.getCartProduct().getPrice());
			}
		}

		return cartDao.save(existingCustomer.getCustomerCart());
}

	@Override
	public Cart getCartProduct(String token) {
		
		System.out.println("Inside get cart");
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();

		Integer cartId = existingCustomer.getCustomerCart().getCartId();

		Optional<Cart> optCart= cartDao.findById(cartId);
		
		if(optCart.isEmpty()) {
			throw new CartItemNotFound("cart Not found by Id");
		}
		return optCart.get();
	}
	
	@Override
	public Cart removeProductFromCart(CartDTO cartDto, String token) {
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		Cart customerCart = existingCustomer.getCustomerCart();
		
		List<CartItem> cartItems = customerCart.getCartItems();
		
		if(cartItems.isEmpty()) {
			throw new CartItemNotFound("Cart is empty");
		}

		boolean flag = false;
		
		for(CartItem c: cartItems) {
			System.out.println("Item" + c.getCartProduct());
			if(c.getCartProduct().getProductId() == cartDto.getProductId()) {
				c.setCartItemQuantity(c.getCartItemQuantity() - 1);
				
				customerCart.setCartTotal(customerCart.getCartTotal() - c.getCartProduct().getPrice());
				if(c.getCartItemQuantity() == 0) {
					
					cartItems.remove(c);

					return cartDao.save(customerCart);
				}
				flag = true;
			}
		}
		
		if(!flag) {
			throw new CartItemNotFound("Product not added to cart");
		}
		
		if(cartItems.isEmpty()) {
			cartDao.save(customerCart);
			throw new CartItemNotFound("Cart is empty now");
		}

		return cartDao.save(customerCart);
	}

	@Override
	public Cart clearCart(String token) {
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		Cart customerCart = existingCustomer.getCustomerCart();
		
		if(customerCart.getCartItems().isEmpty()) {
			throw new CartItemNotFound("Cart already empty");
		}
		
		List<CartItem> emptyCart = new ArrayList<>();
		
		customerCart.setCartItems(emptyCart);
		
		customerCart.setCartTotal(0.0);
		
		return cartDao.save(customerCart);
	}
}
