package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.ProductBo;
import com.example.demo.services.SearchService;

import java.util.*;

@RestController
public class SearchController {
	
	@Autowired
	private SearchService service;

	@GetMapping("/search/{query}")
	public List<ProductBo> search(@PathVariable("query") String query){
		
		return service.searchByName(query);
		
	}
	
	@GetMapping("/search/categories/{query}")
    public List<ProductBo> searchByCategory(@PathVariable("query") String query){
		
		return service.searchByCategory(query);
		
	}
	
	@GetMapping("/search/subcategories/{query}")
    public List<ProductBo> searchBySubCategory(@PathVariable("query") String query){
		
		return service.searchBySubCategory(query);
		
	}
}
