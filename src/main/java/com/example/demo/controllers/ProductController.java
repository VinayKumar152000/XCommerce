package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.ProductBo;
import com.example.demo.payload.ProductPayload;
import com.example.demo.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping
	public List<ProductBo> getAllProducts() {
		return service.getAllProducts();
	}

	@GetMapping("/{id}")
	public ProductBo getSingleProduct(@PathVariable(value = "id") int productId) {
		return service.getProductById(productId);
	}

	@PostMapping
	public ProductBo createProduct(@ModelAttribute ProductPayload product) {
		return service.createProduct(product);
	}

	@PutMapping("/{id}")
	public ProductBo updateProduct(@ModelAttribute ProductPayload product, @PathVariable("id") int productId) {
		return service.updateProduct(product, productId);
	}

	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable("id") int productId) {
		service.deleteProduct(productId);
	}
}
