package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.bo.ProductBo;
import com.example.demo.domain.Category;
import com.example.demo.domain.Product;
import com.example.demo.domain.SubCategory;
import com.example.demo.domain.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SubCategoryRepository;
import com.example.demo.repository.UserRepository;

@Service
public class SearchService {

	@Autowired
	private UserRepository userrepo;

	@Autowired
	private CategoryRepository categoryrepo;

	@Autowired
	private ProductRepository repo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SubCategoryRepository subrepo;

	public List<ProductBo> searchByName(String query) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = this.userrepo.findByEmail(email);

		List<Product> products = this.repo.findByNameOrDescriptionContainingAndUser(query,query,user);

		List<ProductBo> listbo = new ArrayList<>();

		System.out.println(products);
		for (Product product : products) {
			ProductBo productbo = ProducttoProductBo(product);
			listbo.add(productbo);
		}

		return listbo;
	}

	public List<ProductBo> searchByCategory(String query) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = this.userrepo.findByEmail(email);
		Category category = this.categoryrepo.findByName(query);
		
		//find based on multiple categories,we can use findbyCategoriesInandUser(Collection of categories,user)
//		List<Category> cat = new ArrayList<>();
//		cat.add(category);
//		List<Product> prod = this.repo.findByCategoriesIn(cat);
//		System.out.println(prod);

		List<Product> products = this.repo.findByCategoriesContainingAndUser(category, user);

		List<ProductBo> listbo = new ArrayList<>();

		for (Product product : products) {
			ProductBo productbo = ProducttoProductBo(product);
			listbo.add(productbo);
		}

		return listbo;
	}

	public List<ProductBo> searchBySubCategory(String query) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = this.userrepo.findByEmail(email);
		SubCategory subcategory = this.subrepo.findByName(query);
		List<Product> products = this.repo.findBySubcategoriesContainingAndUser(subcategory, user);

		List<ProductBo> listbo = new ArrayList<>();

		for (Product product : products) {
			ProductBo productbo = ProducttoProductBo(product);
			listbo.add(productbo);
		}

		return listbo;
	}

	public ProductBo ProducttoProductBo(Product product) {
		ProductBo productbo = this.modelMapper.map(product, ProductBo.class);
		return productbo;
	}
}
