package com.spring.E_commerce_backend.service;

import com.spring.E_commerce_backend.exception.ProductNotFoundException;
import com.spring.E_commerce_backend.models.CartDTO;
import com.spring.E_commerce_backend.models.CartItem;
import com.spring.E_commerce_backend.models.Product;
import com.spring.E_commerce_backend.models.ProductStatus;
import com.spring.E_commerce_backend.repository.ProductDao;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService{

	private final ProductDao productDao;

    public CartItemServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
	public CartItem createItemforCart(CartDTO cartdto) {
		
		Product existingProduct = productDao.findById(cartdto.getProductId()).orElseThrow( () -> new ProductNotFoundException("Product Not found"));
		
		if(existingProduct.getStatus().equals(ProductStatus.OUTOFSTOCK) || existingProduct.getQuantity() == 0) {
			throw new ProductNotFoundException("Product OUT OF STOCK");
		}
		
		CartItem newItem = new CartItem();
		
		newItem.setCartItemQuantity(1);
		
		newItem.setCartProduct(existingProduct);
		
		return newItem;
	}
}
