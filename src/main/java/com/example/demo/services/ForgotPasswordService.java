package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.payload.ForgotPasswordPayload;
import com.example.demo.repository.UserRepository;

@Service
public class ForgotPasswordService {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private EmailService emailService;

	public int forgotPassword(ForgotPasswordPayload payload) throws Exception {
		String email = payload.getEmail();

		User user = repo.findByEmail(email);

		if (user == null) {
			throw new Exception("User not found with email given");
		}

		return emailService.forgotPasswordEmail(user);
	}
}
