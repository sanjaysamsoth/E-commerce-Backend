package com.spring.E_commerce_backend.controller;

import java.util.List;

import com.spring.E_commerce_backend.models.CategoryEnum;
import com.spring.E_commerce_backend.models.Product;
import com.spring.E_commerce_backend.models.ProductDTO;
import com.spring.E_commerce_backend.models.ProductStatus;
import com.spring.E_commerce_backend.service.ProductService;
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
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductController {

	private final ProductService pService;

    public ProductController(ProductService pService) {
        this.pService = pService;
    }

	@PostMapping("/products")
	public ResponseEntity<Product> addProductToCatalogHandler(@RequestHeader("token") String token,
															  @Valid @RequestBody Product product) {

		Product prod = pService.addProductToCatalog(token, product);

		return new ResponseEntity<>(prod, HttpStatus.ACCEPTED);

	}

	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductFromCatalogByIdHandler(@PathVariable("id") Integer id) {

		Product prod = pService.getProductFromCatalogById(id);

		return new ResponseEntity<>(prod, HttpStatus.FOUND);
	}

	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProductFromCatalogHandler(@PathVariable("id") Integer id) {
		
		String res = pService.deleteProductFromCatalog(id);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PutMapping("/products")
	public ResponseEntity<Product> updateProductInCatalogHandler(@Valid @RequestBody Product prod) {

		Product prod1 = pService.updateProductIncatalog(prod);

		return new ResponseEntity<>(prod1, HttpStatus.OK);
	}

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProductsHandler() {

		List<Product> list = pService.getAllProductsIncatalog();

		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/products/seller/{id}")
	public ResponseEntity<List<ProductDTO>> getAllProductsOfSellerHandler(@PathVariable("id") Integer id) {

		List<ProductDTO> list = pService.getAllProductsOfSeller(id);

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/products/{catenum}")
	public ResponseEntity<List<ProductDTO>> getAllProductsInCategory(@PathVariable("catenum") String catenum) {
		CategoryEnum ce = CategoryEnum.valueOf(catenum.toUpperCase());
		List<ProductDTO> list = pService.getProductsOfCategory(ce);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/products/status/{status}")
	public ResponseEntity<List<ProductDTO>> getProductsWithStatusHandler(@PathVariable("status") String status) {

		ProductStatus ps = ProductStatus.valueOf(status.toUpperCase());
		List<ProductDTO> list = pService.getProductsOfStatus(ps);

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateQuantityOfProduct(@PathVariable("id") Integer id,@RequestBody ProductDTO prodDto){
		
		 Product prod =   pService.updateProductQuantityWithId(id, prodDto);
		
		 return new ResponseEntity<>(prod,HttpStatus.ACCEPTED);
	}
}
