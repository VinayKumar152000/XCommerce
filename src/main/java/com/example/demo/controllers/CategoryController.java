package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.CategoryBo;
import com.example.demo.payload.CategoryPayload;
import com.example.demo.services.CategoryService;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;
	
	@GetMapping
	public List<CategoryBo> getAllCategories() {
		return service.getAllCategories();
	}

	@GetMapping("/{id}")
	public CategoryBo getSingleCategory(@PathVariable(value = "id") int categoryId) {
		return service.getCategoryById(categoryId);
	}

	@PostMapping
	public CategoryBo createCategory(@RequestBody CategoryPayload category) {
		return service.createCategory(category);
	}

	@PutMapping("/{id}")
	public CategoryBo updateCategory(@RequestBody CategoryPayload category, @PathVariable("id") int categoryId) {
		return service.updateCategory(category, categoryId);
	}

	@DeleteMapping("/{id}")
	public void deleteCategory(@PathVariable("id") int categoryId) {
		service.deleteCategory(categoryId);
	}
}
