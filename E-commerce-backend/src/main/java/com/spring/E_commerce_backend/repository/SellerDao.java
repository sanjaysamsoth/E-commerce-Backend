package com.spring.E_commerce_backend.repository;

import java.util.Optional;

import com.spring.E_commerce_backend.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerDao extends JpaRepository<Seller, Integer> {
	
	Optional<Seller> findByMobile(String mobile);
	
}
