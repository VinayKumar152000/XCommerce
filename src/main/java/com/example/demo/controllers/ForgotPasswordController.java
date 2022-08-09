package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.payload.ForgotPasswordPayload;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.ForgotPasswordService;

@RestController
@RequestMapping("api/forgotpassword")
public class ForgotPasswordController {

	@Autowired
	private UserRepository repo;

	@Autowired
	private ForgotPasswordService service;

	@PostMapping
	public int forgotPassword(@RequestBody ForgotPasswordPayload payload) throws Exception {
		return service.forgotPassword(payload);
	}
}
