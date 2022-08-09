package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.bo.SubCategoryBo;
import com.example.demo.domain.SubCategory;
import com.example.demo.exceptions.InvalidInfoException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payload.SubCategoryPayload;
import com.example.demo.repository.SubCategoryRepository;

@Service
public class SubCategoryService {

	@Autowired
	private SubCategoryRepository repo;

	@Autowired
	private ModelMapper modelMapper;

	public List<SubCategoryBo> getAllSubCategories() {

		List<SubCategory> list = this.repo.findAll();
		List<SubCategoryBo> listbo = new ArrayList<>();

		for (SubCategory subcategory : list) {
			SubCategoryBo subcategorybo = SubCategorytoSubCategoryBo(subcategory);
			listbo.add(subcategorybo);
		}

		return listbo;
	}

	public SubCategoryBo getSubCategoryById(int subcategoryId) {
		Optional<SubCategory> optional = this.repo.findById(subcategoryId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("SubCategory", "subcategoryId", subcategoryId);
		}

		SubCategory subcategory = optional.get();
		SubCategoryBo subcategorybo = SubCategorytoSubCategoryBo(subcategory);
		return subcategorybo;

	}

	public SubCategoryBo createSubCategory(SubCategoryPayload subcategory) {

		if (subcategory.getName().equals("") || subcategory.getDescription().equals("")) {
			throw new InvalidInfoException("Please Enter a Valid Role Info", HttpStatus.BAD_REQUEST);
		}

		SubCategory sub = new SubCategory();
		sub.setName(subcategory.getName());
		sub.setDescription(subcategory.getDescription());
		this.repo.save(sub);
		SubCategory subcategories = this.repo.findByName(subcategory.getName());
		SubCategoryBo subcategorybo = SubCategorytoSubCategoryBo(subcategories);

		return subcategorybo;
	}

	public SubCategoryBo updateSubCategory(SubCategoryPayload subcategory, int subcategoryId) {
		Optional<SubCategory> optional = this.repo.findById(subcategoryId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("SubCategory", "subcategoryId", subcategoryId);
		}
		if (subcategory.getName().equals("")) {
			throw new InvalidInfoException("Please Enter a Valid Role Info", HttpStatus.BAD_REQUEST);
		}
		SubCategory existingSubCategory = optional.get();
		existingSubCategory.setName(subcategory.getName());

		this.repo.save(existingSubCategory);
		SubCategoryBo subcategorybo = SubCategorytoSubCategoryBo(existingSubCategory);

		return subcategorybo;
	}

	public String deleteSubCategory(int subcategoryId) {
		Optional<SubCategory> optional = this.repo.findById(subcategoryId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("SubCategory", "subcategoryId", subcategoryId);
		}

		SubCategory existingSubCategory= optional.get();
		this.repo.delete(existingSubCategory);
		return "Roleis Deleted with " + subcategoryId;
	}

	public SubCategoryBo SubCategorytoSubCategoryBo(SubCategory subcategory) {
		SubCategoryBo subcategorybo = this.modelMapper.map(subcategory, SubCategoryBo.class);
		return subcategorybo;
	}
}
