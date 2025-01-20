package com.spring.E_commerce_backend.repository;

import com.spring.E_commerce_backend.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartItemDao extends JpaRepository<CartItem, Integer>{
}
