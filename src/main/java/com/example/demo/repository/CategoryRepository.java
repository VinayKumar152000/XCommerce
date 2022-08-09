package com.example.demo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	Category findByName(String name);
	List<Category> findByNameIn(Collection<String> categories);
}
