package com.spring.E_commerce_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spring.E_commerce_backend.exception.CustomerException;
import com.spring.E_commerce_backend.exception.CustomerNotFoundException;
import com.spring.E_commerce_backend.exception.LoginException;
import com.spring.E_commerce_backend.models.*;
import com.spring.E_commerce_backend.repository.CustomerDao;
import com.spring.E_commerce_backend.repository.SessionDao;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{

	private final CustomerDao customerDao;

	private final LoginLogoutService loginService;

	private final SessionDao sessionDao;

	public CustomerServiceImpl(CustomerDao customerDao, LoginLogoutService loginService, SessionDao sessionDao) {
		this.customerDao = customerDao;
		this.loginService = loginService;
		this.sessionDao = sessionDao;
	}

	@Override
	public Customer addCustomer(Customer customer) {
				
		customer.setCreatedOn(LocalDateTime.now());
		
		Cart c = new Cart();
		
		System.out.println(c);

		customer.setCustomerCart(c);
		
		customer.setOrders(new ArrayList<>());

		Optional<Customer> existing = customerDao.findByMobileNo(customer.getMobileNo());
		
		if(existing.isPresent())
			throw new CustomerException("Customer already exists. Please try to login with your mobile no");
		
		customerDao.save(customer);
		
		return customer;
	}


	@Override
	public Customer getLoggedInCustomerDetails(String token){
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

        return opt.get();
	}
	
	@Override
	public List<Customer> getAllCustomers(String token) throws CustomerNotFoundException {
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token.");
		}
		
		loginService.checkTokenStatus(token);
		
		List<Customer> customers = customerDao.findAll();
		
		if(customers.isEmpty())
			throw new CustomerNotFoundException("No record exists");
		
		return customers;
	}

	@Override
	public Customer updateCustomer(CustomerUpdateDTO customer, String token) throws CustomerNotFoundException {

		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		Optional<Customer> opt = customerDao.findByMobileNo(customer.getMobileNo());
		
		Optional<Customer> res = customerDao.findByEmailId(customer.getEmailId());
		
		if(opt.isEmpty() && res.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist with given mobile no or email-id");
		
		Customer existingCustomer = null;

		if(opt.isPresent())
			existingCustomer = opt.get();
		else
			existingCustomer = res.get();
		
		UserSession user = sessionDao.findByToken(token).get();
		
		if(existingCustomer.getCustomerId() == user.getUserId()) {
		
			if(customer.getFirstName() != null) {
				existingCustomer.setFirstName(customer.getFirstName());
			}
			
			if(customer.getLastName() != null) {
				existingCustomer.setLastName(customer.getLastName());
			}
			
			if(customer.getEmailId() != null) {
				existingCustomer.setEmailId(customer.getEmailId());
			}
			
			if(customer.getMobileNo() != null) {
				existingCustomer.setMobileNo(customer.getMobileNo());
			}
			
			if(customer.getPassword() != null) {
				existingCustomer.setPassword(customer.getPassword());
			}
			
			if(customer.getAddress() != null) {			
				for(Map.Entry<String, Address> values : customer.getAddress().entrySet()) {
					existingCustomer.getAddress().put(values.getKey(), values.getValue());
				}
			}
			
			customerDao.save(existingCustomer);
			return existingCustomer;
		
		}
		else {
			throw new CustomerException("Error in updating. Verification failed.");
		}
		
	}
	
	@Override
	public Customer updateCustomerMobileNoOrEmailId(CustomerUpdateDTO customerUpdateDTO, String token) throws CustomerNotFoundException {
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		if(customerUpdateDTO.getEmailId() != null) {
			existingCustomer.setEmailId(customerUpdateDTO.getEmailId());
		}

		existingCustomer.setMobileNo(customerUpdateDTO.getMobileNo());
			
		customerDao.save(existingCustomer);
			
		return existingCustomer;
		
	}

	@Override
	public SessionDTO updateCustomerPassword(CustomerDTO customerDTO, String token) {
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();

		if(!customerDTO.getMobileId().equals(existingCustomer.getMobileNo())) {
			throw new CustomerException("Verification error. Mobile number does not match");
		}
		
		existingCustomer.setPassword(customerDTO.getPassword());
		
		customerDao.save(existingCustomer);
		
		SessionDTO session = new SessionDTO();
		
		session.setToken(token);
		
		loginService.logoutCustomer(session);
		
		session.setMessage("Updated password and logged out. Login again with new password");
		
		return session;

	}
	
	@Override
	public Customer updateAddress(Address address, String type, String token) throws CustomerException {
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
			
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		existingCustomer.getAddress().put(type, address);
		
		return customerDao.save(existingCustomer);
	}

	@Override
	public Customer updateCreditCardDetails(String token, CreditCard card) throws CustomerException{
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		existingCustomer.setCreditCard(card);
		
		return customerDao.save(existingCustomer);
	}
	
	@Override
	public SessionDTO deleteCustomer(CustomerDTO customerDTO, String token) throws CustomerNotFoundException {
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		SessionDTO session = new SessionDTO();
		
		session.setMessage("");

		session.setToken(token);
		
		if(existingCustomer.getMobileNo().equals(customerDTO.getMobileId()) 
				&& existingCustomer.getPassword().equals(customerDTO.getPassword())) {
			
			customerDao.delete(existingCustomer);
			
			loginService.logoutCustomer(session);
			
			session.setMessage("Deleted account and logged out successfully");
			
			return session;
		}
		else {
			throw new CustomerException("Verification error in deleting account. Please re-check details");
		}
	}

	@Override
	public Customer deleteAddress(String type, String token) throws CustomerException, CustomerNotFoundException {
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		if(!existingCustomer.getAddress().containsKey(type))
			throw new CustomerException("Address type does not exist");
		
		existingCustomer.getAddress().remove(type);
		
		return customerDao.save(existingCustomer);
	}

	@Override
	public List<Order> getCustomerOrders(String token) throws CustomerException {
		
		if(!token.contains("customer")) {
			throw new LoginException("Invalid session token for customer");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Optional<Customer> opt = customerDao.findById(user.getUserId());
		
		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");
		
		Customer existingCustomer = opt.get();
		
		List<Order> myOrders = existingCustomer.getOrders();
		
		if(myOrders.isEmpty())
			throw new CustomerException("No orders found");
		
		return myOrders;
	}
}
