package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.bo.ProductBo;
import com.example.demo.domain.Category;
import com.example.demo.domain.Product;
import com.example.demo.domain.SubCategory;
import com.example.demo.domain.User;
import com.example.demo.exceptions.InvalidInfoException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payload.ProductPayload;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SubCategoryRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private CategoryRepository categoryrepo;

	@Autowired
	private UserRepository userrepo;

	@Autowired
	private SubCategoryRepository subcategoryrepo;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ModelMapper modelMapper;

	final String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/image";

	public List<ProductBo> getAllProducts() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User exUser = userrepo.findByEmail(email);
		List<ProductBo> listbo = new ArrayList<>();
		if (exUser == null) {
			return listbo;
		}

		List<Product> list = exUser.getProducts();

		for (Product product : list) {
			ProductBo productbo = ProducttoProductBo(product);
			listbo.add(productbo);
		}

		return listbo;
	}

	public ProductBo getProductById(int productId) {
		Optional<Product> optional = this.repo.findById(productId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("Product", "productId", productId);
		}

		Product product = optional.get();
		ProductBo productbo = ProducttoProductBo(product);
		return productbo;

	}

	public ProductBo createProduct(ProductPayload product) {

		if (product.getName().equals("")) {
			throw new InvalidInfoException("Please Enter a Valid Role Info", HttpStatus.BAD_REQUEST);
		}

		Product p = new Product();
		p.setName(product.getName());

		MultipartFile image = product.getImage();
		List<String> subcategories = product.getSubcategories();
		List<SubCategory> subcategorylist = subcategoryrepo.findByNameIn(subcategories);
		List<String> categories = product.getCategories();
		List<Category> categoryList = categoryrepo.findByNameIn(categories);
//		Map<String, List<String>> categories = product.getCategories();
//
//		List<Category> categoryList = new ArrayList<>();
//
//		Set<String> category = categories.keySet();
//
//		for (String c : category) {
//
//			Category cat = categoryrepo.findByName(c);
//			if (cat == null) {
//				throw new InvalidInfoException("Please Enter a Valid Info", HttpStatus.BAD_REQUEST);
//			}
//
//			List<String> subcategory = categories.get(c);
//			if(subcategory.size()==0) {
//				throw new InvalidInfoException("Please Enter a Valid Info", HttpStatus.BAD_REQUEST);
//			}
//			
//			List<SubCategory> subs = subcategoryrepo.findByNameIn(subcategory);
//			cat.setSubcategories(subs);
//			categoryList.add(cat);
//		}

		try {
			boolean flag = storageService.imageUpload(image, uploadDirectory);

			if (flag == false) {
				throw new InvalidInfoException("Image not Uploaded", HttpStatus.BAD_REQUEST);
			}

			p.setImage(ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/")
					.path(image.getOriginalFilename()).toUriString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		p.setCategories(categoryList);
		p.setSubcategories(subcategorylist);

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(email);
		User exUser = userrepo.findByEmail(email);
		p.setUser(exUser);
		this.repo.save(p);
		Product products = this.repo.findByName(product.getName());
		ProductBo productbo = ProducttoProductBo(products);

		return productbo;
	}

	public ProductBo updateProduct(ProductPayload product, int productId) {
		Optional<Product> optional = this.repo.findById(productId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("Product", "productId", productId);
		}
		if (product.getName().equals("")) {
			throw new InvalidInfoException("Please Enter a Valid Role Info", HttpStatus.BAD_REQUEST);
		}

		Product existingProduct = optional.get();
		existingProduct.setName(product.getName());
		MultipartFile image = product.getImage();

		String imagePath = existingProduct.getImage();
		String img[] = imagePath.split("/");

		String path = img[img.length - 1];
		try {
			boolean flag = storageService.imageDelete(path, uploadDirectory);
			if (flag == false) {
				throw new InvalidInfoException("Image not Uploaded", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			boolean flag = storageService.imageUpload(image, uploadDirectory);

			if (flag == false) {
				throw new InvalidInfoException("Image not Uploaded", HttpStatus.BAD_REQUEST);
			}

			existingProduct.setImage(ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/")
					.path(image.getOriginalFilename()).toUriString());
			;
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> categories = product.getCategories();
		List<String> subcategories = product.getSubcategories();

		List<Category> categoryList = categoryrepo.findByNameIn(categories);
		List<SubCategory> subcategoryList = subcategoryrepo.findByNameIn(subcategories);

		existingProduct.setCategories(categoryList);
		existingProduct.setSubcategories(subcategoryList);

		this.repo.save(existingProduct);
		ProductBo productbo = ProducttoProductBo(existingProduct);

		return productbo;
	}

	public String deleteProduct(int productId) {
		Optional<Product> optional = this.repo.findById(productId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("Product", "productId", productId);
		}

		Product existingProduct = optional.get();
		this.repo.delete(existingProduct);
		return "Roleis Deleted with " + productId;
	}

	public ProductBo ProducttoProductBo(Product product) {
		ProductBo productbo = this.modelMapper.map(product, ProductBo.class);
		return productbo;
	}
}
