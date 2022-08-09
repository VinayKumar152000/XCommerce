package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import com.example.demo.domain.Category;
import com.example.demo.domain.Product;
import com.example.demo.domain.SubCategory;
import com.example.demo.domain.User;

public interface ProductRepository  extends JpaRepository<Product, Integer>  {

	public Product findByName(String name);
	
	public List<Product> findByCategoriesContainingAndUser(Category c,User user);
	public List<Product> findByNameOrDescriptionContainingAndUser(String name,String description,User user);
	public List<Product> findBySubcategoriesContainingAndUser(SubCategory c,User user);
	public List<Product> findByCategoriesIn(Collection<Category> categories);
}
