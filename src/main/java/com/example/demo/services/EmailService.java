package com.example.demo.services;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.domain.ForgotPassword;
import com.example.demo.domain.User;
import com.example.demo.repository.ForgotPasswordRepository;

import freemarker.template.Configuration;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration configuration;
	
	@Autowired
	private ForgotPasswordRepository repo;

	public void sendEmail(User user) throws Exception {

		// auto generated password

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		helper.setSubject("Registration Succesfull");
		helper.setTo(user.getEmail());

		int num = (int) (Math.random() * 1000000);
		String password = num + "";

		user.setPassword(password);
//		
		String emailContent = getEmailContent(user);
		helper.setText(emailContent, true);
		mailSender.send(mimeMessage);
		System.out.println("Mailsend successfully");
	}

	String getEmailContent(User user) throws Exception {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		configuration.getTemplate("EmailTemplate.ftlh").process(model, stringWriter);
		return stringWriter.getBuffer().toString();
	}
	
	public int forgotPasswordEmail(User user) throws Exception {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		helper.setSubject("Reset Your Password Using Below Link");
		helper.setTo(user.getEmail());

		int num = (int) (Math.random() * 1000000);
		String response = "http://localhost:8081/reset-password?token=" + num;
		helper.setText(response, true);
		mailSender.send(mimeMessage);

		ForgotPassword forgotpassword = new ForgotPassword();
		forgotpassword.setToken(num + "");
		forgotpassword.setUser(user);

		repo.save(forgotpassword);

		return num;

	}
}
