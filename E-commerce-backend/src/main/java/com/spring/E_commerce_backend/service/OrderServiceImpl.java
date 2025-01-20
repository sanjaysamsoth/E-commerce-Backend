package com.spring.E_commerce_backend.service;

import com.spring.E_commerce_backend.exception.LoginException;
import com.spring.E_commerce_backend.exception.OrderException;
import com.spring.E_commerce_backend.models.*;
import com.spring.E_commerce_backend.repository.OrderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	private final OrderDao oDao;
	private final CustomerService cs;
	private final CartServiceImpl cartService;
	private final NotificationService notificationService;

	@Autowired
	public OrderServiceImpl(OrderDao oDao, CustomerService cs, CartServiceImpl cartService, NotificationService notificationService) {
		this.oDao = oDao;
		this.cs = cs;
		this.cartService = cartService;
		this.notificationService = notificationService;
	}

	@Override
	public Order saveOrder(OrderDTO odto, String token) throws LoginException, OrderException {
		Order newOrder = new Order();

		Customer loggedInCustomer = cs.getLoggedInCustomerDetails(token);

		if (loggedInCustomer != null) {
			newOrder.setCustomer(loggedInCustomer);
			String usersCardNumber = loggedInCustomer.getCreditCard().getCardNumber();
			String userGivenCardNumber = odto.getCardNumber().getCardNumber();
			List<CartItem> productsInCart = loggedInCustomer.getCustomerCart().getCartItems();
			List<CartItem> productsInOrder = new ArrayList<>(productsInCart);

			newOrder.setOrdercartItems(productsInOrder);
			newOrder.setTotal(loggedInCustomer.getCustomerCart().getCartTotal());

			if (!productsInCart.isEmpty()) {
				if ((usersCardNumber.equals(userGivenCardNumber))
						&& (odto.getCardNumber().getCardValidity().equals(loggedInCustomer.getCreditCard().getCardValidity())
						&& (odto.getCardNumber().getCardCVV().equals(loggedInCustomer.getCreditCard().getCardCVV())))) {

					newOrder.setCardNumber(odto.getCardNumber().getCardNumber());
					newOrder.setAddress(loggedInCustomer.getAddress().get(odto.getAddressType()));
					newOrder.setDate(LocalDate.now());
					newOrder.setOrderStatus(OrderStatusValues.SUCCESS);

					for (CartItem cartItem : productsInCart) {
						Integer remainingQuantity = cartItem.getCartProduct().getQuantity() - cartItem.getCartItemQuantity();
						if (remainingQuantity < 0 || cartItem.getCartProduct().getStatus() == ProductStatus.OUTOFSTOCK) {
							logger.error("Product {} is out of stock", cartItem.getCartProduct().getProductName());
							throw new OrderException("Product " + cartItem.getCartProduct().getProductName() + " OUT OF STOCK");
						}
						cartItem.getCartProduct().setQuantity(remainingQuantity);
						if (cartItem.getCartProduct().getQuantity() == 0) {
							cartItem.getCartProduct().setStatus(ProductStatus.OUTOFSTOCK);
						}
					}
					cartService.clearCart(token);
					Order savedOrder = oDao.save(newOrder);

					// Send real-time notification
					notificationService.sendNotificationToUser(loggedInCustomer.getCustomerId().toString(),
							"Order " + savedOrder.getOrderId() + " has been placed successfully.");

					logger.info("Order {} for customer {} has been placed successfully.", savedOrder.getOrderId(), loggedInCustomer.getCustomerId());
					return savedOrder;
				} else {
					newOrder.setCardNumber(null);
					newOrder.setAddress(loggedInCustomer.getAddress().get(odto.getAddressType()));
					newOrder.setDate(LocalDate.now());
					newOrder.setOrderStatus(OrderStatusValues.PENDING);
					cartService.clearCart(token);
					Order savedOrder = oDao.save(newOrder);

					// Send real-time notification
					notificationService.sendNotificationToUser(loggedInCustomer.getCustomerId().toString(),
							"Order " + savedOrder.getOrderId() + " is pending due to payment issues.");

					logger.warn("Order {} for customer {} is pending due to payment issues.", savedOrder.getOrderId(), loggedInCustomer.getCustomerId());
					return savedOrder;
				}
			} else {
				logger.error("No products in the cart for customer {}", loggedInCustomer.getCustomerId());
				throw new OrderException("No products in Cart");
			}

		} else {
			logger.error("Invalid session token for customer.");
			throw new LoginException("Invalid session token for customer. Kindly Login");
		}
	}

	@Override
	public Order getOrderByOrderId(Integer orderId) throws OrderException {
		return oDao.findById(orderId).orElseThrow(() -> new OrderException("No order exists with given OrderId " + orderId));
	}

	@Override
	public List<Order> getAllOrders() throws OrderException {
		List<Order> orders = oDao.findAll();
		if (!orders.isEmpty()) {
			logger.info("Fetched {} orders successfully.", orders.size());
			return orders;
		} else {
			logger.warn("No orders found in the database.");
			throw new OrderException("No Orders exist on your account");
		}
	}

	@Override
	public Order cancelOrderByOrderId(Integer orderId, String token) throws OrderException {
		Order order = oDao.findById(orderId).orElseThrow(() -> new OrderException("No order exists with given OrderId " + orderId));
		Customer loggedInCustomer = cs.getLoggedInCustomerDetails(token);

		if (order.getCustomer().getCustomerId().equals(loggedInCustomer.getCustomerId())) {
			if (order.getOrderStatus() == OrderStatusValues.PENDING || order.getOrderStatus() == OrderStatusValues.SUCCESS) {
				order.setOrderStatus(OrderStatusValues.CANCELLED);
				oDao.save(order);

				// Send real-time notification
				notificationService.sendNotificationToUser(loggedInCustomer.getCustomerId().toString(),
						"Order " + order.getOrderId() + " has been cancelled.");

				logger.info("Order {} for customer {} has been cancelled.", orderId, loggedInCustomer.getCustomerId());
				return order;
			} else {
				logger.warn("Order {} was already cancelled.", orderId);
				throw new OrderException("Order is already cancelled.");
			}
		} else {
			logger.error("Invalid session token for customer.");
			throw new LoginException("Invalid session token for customer. Kindly Login");
		}
	}

	@Override
	public Order updateOrderByOrder(OrderDTO orderdto, Integer orderId, String token) throws OrderException, LoginException {
		Order existingOrder = oDao.findById(orderId).orElseThrow(() -> new OrderException("No order exists with given OrderId " + orderId));
		Customer loggedInCustomer = cs.getLoggedInCustomerDetails(token);

		if (existingOrder.getCustomer().getCustomerId().equals(loggedInCustomer.getCustomerId())) {
			existingOrder.setCardNumber(orderdto.getCardNumber().getCardNumber());
			existingOrder.setAddress(loggedInCustomer.getAddress().get(orderdto.getAddressType()));
			existingOrder.setOrderStatus(OrderStatusValues.SUCCESS);

			Order updatedOrder = oDao.save(existingOrder);

			// Send real-time notification
			notificationService.sendNotificationToUser(loggedInCustomer.getCustomerId().toString(),
					"Order " + updatedOrder.getOrderId() + " has been updated successfully.");

			logger.info("Order {} for customer {} has been updated successfully.", updatedOrder.getOrderId(), loggedInCustomer.getCustomerId());
			return updatedOrder;
		} else {
			logger.error("Invalid session token for customer.");
			throw new LoginException("Invalid session token for customer. Kindly Login");
		}
	}

	@Override
	public List<Order> getAllOrdersByDate(LocalDate date) throws OrderException {
		List<Order> orders = oDao.findByDate(date);
		if (!orders.isEmpty()) {
			logger.info("Fetched {} orders placed on {}.", orders.size(), date);
			return orders;
		} else {
			logger.warn("No orders found on {}.", date);
			throw new OrderException("No orders found on the specified date");
		}
	}

	@Override
	public Customer getCustomerByOrderid(Integer orderId) throws OrderException {
		Optional<Order> opt = oDao.findById(orderId);
		if (opt.isPresent()) {
			Order existingOrder = opt.get();
			Customer customer = existingOrder.getCustomer();
			logger.info("Customer {} retrieved for order {}.", customer.getCustomerId(), orderId);
			return customer;
		} else {
			logger.error("No order exists with orderId {}.", orderId);
			throw new OrderException("No Order exists with orderId " + orderId);
		}
	}
}
