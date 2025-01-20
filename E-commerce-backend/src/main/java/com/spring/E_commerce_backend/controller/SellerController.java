package com.spring.E_commerce_backend.controller;

import java.util.List;

import com.spring.E_commerce_backend.models.Seller;
import com.spring.E_commerce_backend.models.SellerDTO;
import com.spring.E_commerce_backend.models.SessionDTO;
import com.spring.E_commerce_backend.service.SellerService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SellerController {
	
	private final SellerService sService;

	public SellerController(SellerService sService) {
		this.sService = sService;
	}

	@PostMapping("/addseller")
	public ResponseEntity<Seller> addSellerHandler(@Valid @RequestBody Seller seller){
		
		Seller addseller=sService.addSeller(seller);
		
		System.out.println("Seller"+ seller);
		
		return new ResponseEntity<>(addseller,HttpStatus.CREATED);
	}
	
	@GetMapping("/sellers")
	public ResponseEntity<List<Seller>> getAllSellerHandler(){
		
		List<Seller> sellers=sService.getAllSellers();
		
		return new ResponseEntity<>(sellers, HttpStatus.OK);
	}
	
	@GetMapping("/seller/{sellerId}")
	public ResponseEntity<Seller> getSellerByIdHandler(@PathVariable("sellerId") Integer Id){
		
		Seller getSeller=sService.getSellerById(Id);
		
		return new ResponseEntity<>(getSeller, HttpStatus.OK);
	}

	@GetMapping("/seller")
	public ResponseEntity<Seller> getSellerByMobileHandler(@RequestParam("mobile") String mobile, @RequestHeader("token") String token){
		
		Seller getSeller=sService.getSellerByMobile(mobile, token);
		
		return new ResponseEntity<>(getSeller, HttpStatus.OK);
	}

	@GetMapping("/seller/current")
	public ResponseEntity<Seller> getLoggedInSellerHandler(@RequestHeader("token") String token){
		
		Seller getSeller = sService.getCurrentlyLoggedInSeller(token);
		
		return new ResponseEntity<>(getSeller, HttpStatus.OK);
	}

	@PutMapping("/seller")
	public ResponseEntity<Seller> updateSellerHandler(@RequestBody Seller seller, @RequestHeader("token") String token){
		Seller updatedseller=sService.updateSeller(seller, token);
		
		return new ResponseEntity<>(updatedseller,HttpStatus.ACCEPTED);
		
	}

	@PutMapping("/seller/update/mobile")
	public ResponseEntity<Seller> updateSellerMobileHandler(@Valid @RequestBody SellerDTO sellerdto, @RequestHeader("token") String token){
		Seller updatedseller=sService.updateSellerMobile(sellerdto, token);
		
		return new ResponseEntity<>(updatedseller,HttpStatus.ACCEPTED);
		
	}

	@PutMapping("/seller/update/password")
	public ResponseEntity<SessionDTO> updateSellerPasswordHandler(@Valid @RequestBody SellerDTO sellerDto, @RequestHeader("token") String token){
		return new ResponseEntity<>(sService.updateSellerPassword(sellerDto, token), HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/seller/{sellerId}")
	public ResponseEntity<Seller> deleteSellerByIdHandler(@PathVariable("sellerId") Integer Id, @RequestHeader("token") String token){
		
		Seller deletedSeller=sService.deleteSellerById(Id, token);
		
		return new ResponseEntity<>(deletedSeller,HttpStatus.OK);
		
	}
}
