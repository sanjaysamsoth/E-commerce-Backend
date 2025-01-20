package com.spring.E_commerce_backend.repository;

import java.util.Map;

import com.spring.E_commerce_backend.models.Cart;
import com.spring.E_commerce_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartDao extends JpaRepository<Cart,Integer> {

 //public Map<Product,Integer> findbyName(String productName);
 //public Cart findbyId(Integer cartId);

}
