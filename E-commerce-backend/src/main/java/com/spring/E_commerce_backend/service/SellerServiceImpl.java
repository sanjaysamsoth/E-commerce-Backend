package com.spring.E_commerce_backend.service;

import java.util.List;
import java.util.Optional;

import com.spring.E_commerce_backend.exception.LoginException;
import com.spring.E_commerce_backend.exception.SellerException;
import com.spring.E_commerce_backend.models.Seller;
import com.spring.E_commerce_backend.models.SellerDTO;
import com.spring.E_commerce_backend.models.SessionDTO;
import com.spring.E_commerce_backend.models.UserSession;
import com.spring.E_commerce_backend.repository.SellerDao;
import com.spring.E_commerce_backend.repository.SessionDao;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {

	private final SellerDao sellerDao;

	private final LoginLogoutService loginService;

	private final SessionDao sessionDao;

	public SellerServiceImpl(SellerDao sellerDao, LoginLogoutService loginService, SessionDao sessionDao) {
		this.sellerDao = sellerDao;
		this.loginService = loginService;
		this.sessionDao = sessionDao;
	}

	@Override
	public Seller addSeller(Seller seller) {

		return sellerDao.save(seller);
	}

	@Override
	public List<Seller> getAllSellers() throws SellerException {
		
		List<Seller> sellers= sellerDao.findAll();
		
		if(!sellers.isEmpty()) {
			return sellers;
		}
		else throw new SellerException("No Seller Found !");
	}

	@Override
	public Seller getSellerById(Integer sellerId) {
		
		Optional<Seller> seller=sellerDao.findById(sellerId);
		
		if(seller.isPresent()) {
			return seller.get();
		}
		else throw new SellerException("Seller not found for this ID: "+sellerId);
	}

	@Override
	public Seller updateSeller(Seller seller, String token) {
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);

		return sellerDao.save(seller);
	}

	@Override
	public Seller deleteSellerById(Integer sellerId, String token) {
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);
		
		Optional<Seller> opt=sellerDao.findById(sellerId);
		
		if(opt.isPresent()) {
			
			UserSession user = sessionDao.findByToken(token).get();
			
			Seller existingseller=opt.get();
			
			if(user.getUserId() == existingseller.getSellerId()) {
				sellerDao.delete(existingseller);
				
				SessionDTO session = new SessionDTO();
				session.setToken(token);
				loginService.logoutSeller(session);
				
				return existingseller;
			}
			else {
				throw new SellerException("Verification Error in deleting seller account");
			}
		}
		else throw new SellerException("Seller not found for this ID: "+sellerId);
		
	}

	@Override
	public Seller updateSellerMobile(SellerDTO sellerdto, String token) throws SellerException {
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();
		
		Seller existingSeller=sellerDao.findById(user.getUserId()).orElseThrow(()->new SellerException("Seller not found for this ID: "+ user.getUserId()));
		
		if(existingSeller.getPassword().equals(sellerdto.getPassword())) {
			existingSeller.setMobile(sellerdto.getMobile());
			return sellerDao.save(existingSeller);
		}
		else {
			throw new SellerException("Error occured in updating mobile. Please enter correct password");
		}
		
	}

	@Override
	public Seller getSellerByMobile(String mobile, String token) throws SellerException {
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);

		return sellerDao.findByMobile(mobile).orElseThrow( () -> new SellerException("Seller not found with given mobile"));
	}
	
	@Override
	public Seller getCurrentlyLoggedInSeller(String token) throws SellerException{
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionDao.findByToken(token).get();

        return sellerDao.findById(user.getUserId()).orElseThrow(()->new SellerException("Seller not found for this ID"));
		
	}

	@Override
	public SessionDTO updateSellerPassword(SellerDTO sellerDTO, String token) {
				
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
			
		loginService.checkTokenStatus(token);
			
		UserSession user = sessionDao.findByToken(token).get();
			
		Optional<Seller> opt = sellerDao.findById(user.getUserId());
			
		if(opt.isEmpty())
			throw new SellerException("Seller does not exist");
			
		Seller existingSeller = opt.get();
			
			
		if(!sellerDTO.getMobile().equals(existingSeller.getMobile())) {
			throw new SellerException("Verification error. Mobile number does not match");
		}
			
		existingSeller.setPassword(sellerDTO.getPassword());
			
		sellerDao.save(existingSeller);
			
		SessionDTO session = new SessionDTO();
			
		session.setToken(token);
			
		loginService.logoutSeller(session);
			
		session.setMessage("Updated password and logged out. Login again with new password");
			
		return session;

	}
}
