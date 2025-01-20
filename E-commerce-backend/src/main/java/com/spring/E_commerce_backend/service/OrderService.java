package com.spring.E_commerce_backend.service;

import com.spring.E_commerce_backend.exception.LoginException;
import com.spring.E_commerce_backend.exception.OrderException;
import com.spring.E_commerce_backend.models.Customer;
import com.spring.E_commerce_backend.models.Order;
import com.spring.E_commerce_backend.models.OrderDTO;

import java.time.LocalDate;
import java.util.List;


public interface OrderService {

	Order saveOrder(OrderDTO odto, String token) throws LoginException, OrderException;

	Order getOrderByOrderId(Integer orderId) throws OrderException;

	List<Order> getAllOrders() throws OrderException;

	Order cancelOrderByOrderId(Integer orderId, String token) throws OrderException;

	Order updateOrderByOrder(OrderDTO order, Integer orderId, String token) throws OrderException, LoginException;

	List<Order> getAllOrdersByDate(LocalDate date) throws OrderException;

	Customer getCustomerByOrderid(Integer orderId) throws OrderException;
	
}
