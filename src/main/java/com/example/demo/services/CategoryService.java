package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.bo.CategoryBo;

import com.example.demo.domain.Category;
import com.example.demo.domain.SubCategory;
import com.example.demo.exceptions.InvalidInfoException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payload.CategoryPayload;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.SubCategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	@Autowired
	private SubCategoryRepository subrepo;

	@Autowired
	private ModelMapper modelMapper;

	public List<CategoryBo> getAllCategories() {

		List<Category> list = this.repo.findAll();
		List<CategoryBo> listbo = new ArrayList<>();

		for (Category category : list) {
			CategoryBo categorybo = CategorytoCategoryBo(category);
			listbo.add(categorybo);
		}

		return listbo;
	}

	public CategoryBo getCategoryById(int categoryId) {
		Optional<Category> optional = this.repo.findById(categoryId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("Category", "categoryId", categoryId);
		}

		Category category = optional.get();
		CategoryBo categorybo = CategorytoCategoryBo(category);
		return categorybo;

	}

	public CategoryBo createCategory(CategoryPayload category) {

		if (category.getName().equals("") || category.getDescription().equals("")) {
			throw new InvalidInfoException("Please Enter a Valid Role Info", HttpStatus.BAD_REQUEST);
		}

		List<String> subcategories = category.getSubcategories();
		List<SubCategory> subs = subrepo.findByNameIn(subcategories);

		Category sub = new Category();
		sub.setName(category.getName());
		sub.setDescription(category.getDescription());
		sub.setSubcategories(subs);

		this.repo.save(sub);
		Category categories = this.repo.findByName(category.getName());
		CategoryBo categorybo = CategorytoCategoryBo(categories);

		return categorybo;
	}

	public CategoryBo updateCategory(CategoryPayload category, int categoryId) {
		Optional<Category> optional = this.repo.findById(categoryId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("Category", "categoryId", categoryId);
		}
		if (category.getName().equals("")) {
			throw new InvalidInfoException("Please Enter a Valid Role Info", HttpStatus.BAD_REQUEST);
		}

		List<String> subcategories = category.getSubcategories();
		List<SubCategory> subs = subrepo.findByNameIn(subcategories);

		Category existingCategory = optional.get();
		existingCategory.setName(category.getName());
		existingCategory.setDescription(category.getDescription());
		existingCategory.setSubcategories(subs);

		this.repo.save(existingCategory);
		CategoryBo categorybo = CategorytoCategoryBo(existingCategory);

		return categorybo;
	}

	public String deleteCategory(int categoryId) {
		Optional<Category> optional = this.repo.findById(categoryId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("Category", "categoryId", categoryId);
		}

		Category existingCategory = optional.get();
		this.repo.delete(existingCategory);
		return "Category is Deleted with " + categoryId;
	}

	public CategoryBo CategorytoCategoryBo(Category category) {
		CategoryBo categorybo = this.modelMapper.map(category, CategoryBo.class);
		return categorybo;
	}
}
