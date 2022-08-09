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

import com.example.demo.bo.SubCategoryBo;

import com.example.demo.payload.SubCategoryPayload;
import com.example.demo.services.SubCategoryService;

@RestController
@RequestMapping("api/subcategories")
public class SubCategoryController {

	@Autowired
	private SubCategoryService service;
	
	@GetMapping
	public List<SubCategoryBo> getAllSubCategories() {
		return service.getAllSubCategories();
	}

	@GetMapping("/{id}")
	public SubCategoryBo getSingleSubCategory(@PathVariable(value = "id") int subcategoryId) {
		return service.getSubCategoryById(subcategoryId);
	}

	@PostMapping
	public SubCategoryBo createSubCategory(@RequestBody SubCategoryPayload subcategory) {
		return service.createSubCategory(subcategory);
	}

	@PutMapping("/{id}")
	public SubCategoryBo updateSubCategory(@RequestBody SubCategoryPayload subcategory, @PathVariable("id") int subcategoryId) {
		return service.updateSubCategory(subcategory, subcategoryId);
	}

	@DeleteMapping("/{id}")
	public void deleteSubCategory(@PathVariable("id") int subcategoryId) {
		service.deleteSubCategory(subcategoryId);
	}
}
