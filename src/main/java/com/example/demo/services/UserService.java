package com.example.demo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.bo.UserBo;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.exceptions.InvalidInfoException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payload.UserPayload;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private RoleRepository rolerepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private StorageService storageService;

	@Autowired
	private EmailService emailService;


	public static String path = System.getProperty("user.dir") + "/src/main/resources/static/image";

	public List<UserBo> getAllUsers() {
		List<User> list = this.repo.findAll();
		List<UserBo> listbo = new ArrayList<>();

		for (User user : list) {
			UserBo userbo = UsertoUserBo(user);
			listbo.add(userbo);
		}

		return listbo;
	}

	public UserBo getUser(int userId) {

		Optional<User> optional = this.repo.findById(userId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("User", "userId", userId);
		}

		User user = optional.get();

		UserBo userbo = UsertoUserBo(user);
		return userbo;
	}

	public UserBo createUser(UserPayload user) throws Exception {

		String name = user.getName();
		String address = user.getAddress();
		String email = user.getEmail();
		List<String> roles = user.getRoles();
		MultipartFile image = user.getImage();
		String password = user.getPassword();
		if (name.equals("") || address.equals("") || email.equals("") || roles.size() == 0 || image.isEmpty()
				|| password.equals("")) {
			throw new InvalidInfoException("All Fields are Mandatory Please Enter Valid Data", HttpStatus.BAD_REQUEST);
		}

		List<Role> roleslist = this.rolerepo.findByNameIn(roles);

		if (roleslist.size() == 0) {
			throw new InvalidInfoException("Please Enter Valid Roles Data", HttpStatus.BAD_REQUEST);
		}

		User u = new User();
		u.setName(name);
		u.setAddress(address);
		u.setEmail(email);
		u.setPassword(password);
		u.setRoles(roleslist);

//		System.out.println(new ClassPathResource("").getFile().getAbsolutePath());
		// file uploading
		// mention the upload directory for images
		try {
			boolean flag = storageService.imageUpload(image, path);

			if (flag == false) {
				throw new InvalidInfoException("Image not Uploaded", HttpStatus.BAD_REQUEST);
			}

			u.setImage(ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/")
					.path(image.getOriginalFilename()).toUriString());
			;
		} catch (Exception e) {
			e.printStackTrace();
		}

//		emailService.sendEmail(u);
		this.repo.save(u);

		User users = this.repo.findByName(name);
		UserBo userbo = UsertoUserBo(users);
		return userbo;
	}

	public UserBo updateUser(UserPayload user, int userId) {

		Optional<User> optional = this.repo.findById(userId);
		User existingUser = optional.get();

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("User", "userId", userId);
		}

		String name = user.getName();
		String address = user.getAddress();
		String email = user.getEmail();
		List<String> roles = user.getRoles();
		String password = user.getPassword();
		MultipartFile image = user.getImage();

		if (name.equals("") || address.equals("") || email.equals("") || roles.size() == 0) {
			throw new InvalidInfoException("All Fields are Mandatory Please Enter Valid Data", HttpStatus.BAD_REQUEST);
		}

		List<Role> roleslist = this.rolerepo.findByNameIn(roles);

		if (roleslist.size() == 0) {
			throw new InvalidInfoException("Please Enter Valid Roles Data", HttpStatus.BAD_REQUEST);
		}

		String imagePath = existingUser.getImage();
		String img[] = imagePath.split("/");

		String imageName = img[img.length - 1];
		try {
			boolean flag = storageService.imageDelete(imageName, path);
			if (flag == false) {
				throw new InvalidInfoException("Image not Deleted", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			boolean flag = storageService.imageUpload(image, path);

			if (flag == false) {
				throw new InvalidInfoException("Image not Uploaded", HttpStatus.BAD_REQUEST);
			}

			existingUser.setImage(ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/")
					.path(image.getOriginalFilename()).toUriString());
			;
		} catch (Exception e) {
			e.printStackTrace();
		}

		existingUser.setName(name);
		existingUser.setAddress(address);
		existingUser.setEmail(email);

		existingUser.setRoles(roleslist);

		existingUser.setPassword(password);

		this.repo.save(existingUser);
		UserBo userbo = UsertoUserBo(existingUser);
		return userbo;
	}

	public void deleteUser(int userId) {

		Optional<User> optional = this.repo.findById(userId);

		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("User", "userId", userId);
		}

		User existingUser = optional.get();
		this.repo.delete(existingUser);
		return;
	}

	public UserBo UsertoUserBo(User user) {
		UserBo userbo = this.modelMapper.map(user, UserBo.class);
		return userbo;
	}
}
