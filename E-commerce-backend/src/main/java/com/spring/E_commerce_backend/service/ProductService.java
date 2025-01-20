package com.spring.E_commerce_backend.service;

import com.spring.E_commerce_backend.models.CategoryEnum;
import com.spring.E_commerce_backend.models.Product;
import com.spring.E_commerce_backend.models.ProductDTO;
import com.spring.E_commerce_backend.models.ProductStatus;

import java.util.List;

public interface ProductService {

	public Product addProductToCatalog(String token, Product product);

	public Product getProductFromCatalogById(Integer id);

	public String deleteProductFromCatalog(Integer id);

	public Product updateProductIncatalog(Product product);
	
	public List<Product> getAllProductsIncatalog();
	
	public List<ProductDTO> getAllProductsOfSeller(Integer id);
	
	public List<ProductDTO> getProductsOfCategory(CategoryEnum catenum);
	
	public List<ProductDTO> getProductsOfStatus(ProductStatus status);
	
	public Product updateProductQuantityWithId(Integer id,ProductDTO prodDTO);

}
