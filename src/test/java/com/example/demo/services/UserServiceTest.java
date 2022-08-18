package com.example.demo.services;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.bo.UserBo;
import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private UserMapper userMapper;
	@Mock
	private StorageService storageService;
	@Mock
	private EmailService emailService;

	@Mock
	private UserService userService;

	// junit lifecycle method
	@BeforeEach
	public void setup() {
		userService = new UserService(userRepository, roleRepository, userMapper, storageService, emailService);
	}

	@Test
	@DisplayName("Should Retrieve User by Id")
	public void shouldFindUserById() {

		User user = new User(3, "Vinay Kumar", "Kangra,Himachal Pradesh", "csecec.1802899@gmail.com",
				"http://localhost:8081/image/cmdsqlres.JPG", "VinayKumar@15", null, null);

		UserBo expectedUserResponse = new UserBo(3, "Vinay Kumar", "Kangra,Himachal Pradesh",
				"csecec.1802899@gmail.com", null);

		Mockito.when(userRepository.findById(3)).thenReturn(Optional.of(user));
		Mockito.when(userMapper.UsertoUserBo(Mockito.any(User.class))).thenReturn(expectedUserResponse);

		UserBo actualUserResponse = userService.getUser(3);

		Assertions.assertThat(actualUserResponse.getName()).isEqualTo(expectedUserResponse.getName());
	}

	// https://www.youtube.com/watch?v=kXhYu939_5s
	@Test
	@DisplayName("Should Retrive All Users")
	public void shouldSaveUserById() {

		Mockito.when(userRepository.findAll())
				.thenReturn(Stream
						.of(new User(3, "Vinay Kumar", "Kangra,Himachal Pradesh", "csecec.1802899@gmail.com",
								"http://localhost:8081/image/cmdsqlres.JPG", "VinayKumar@15", null, null))
						.collect(Collectors.toList()));
		Mockito.when(userMapper.UsertoUserBo(Mockito.any(User.class)))
				.thenReturn(new UserBo(3, "Vinay Kumar", "Kangra,Himachal Pradesh", "csecec.1802899@gmail.com", null));

		List<UserBo> list = userService.getAllUsers();

		assertEquals(1, list.size());
	}

}
