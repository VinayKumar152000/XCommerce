package com.example.demo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {

	SubCategory findByName(String name);
	List<SubCategory> findByNameIn(Collection<String> subcategories);
}
