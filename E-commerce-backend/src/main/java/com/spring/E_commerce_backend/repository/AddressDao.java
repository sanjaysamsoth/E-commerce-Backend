package com.spring.E_commerce_backend.repository;

import com.spring.E_commerce_backend.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddressDao extends JpaRepository<Address, Integer>{
}
