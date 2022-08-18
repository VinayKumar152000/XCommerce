package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.bo.UserBo;
import com.example.demo.domain.User;

public class UserMapper {
	
	@Autowired
	private ModelMapper modelMapper;

	public UserBo UsertoUserBo(User user) {
		UserBo userbo = this.modelMapper.map(user, UserBo.class);
		return userbo;
	}
}
