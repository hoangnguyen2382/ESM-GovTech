package com.govtech.esm.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;

import com.govtech.esm.entity.UserDetail;
import com.govtech.esm.repository.UserDetailsRepository;
import com.govtech.esm.util.ObjectBuilder;

class UserServiceTest {
	@Mock
	private UserDetailsRepository userRepository;

	@Autowired
	private Validator validator;

	private UserService userService;

	private UserDetail user1 = ObjectBuilder.createUser("id1", "login1", "name1", BigDecimal.valueOf(1000),
			new Date());;
	private UserDetail user2 = ObjectBuilder.createUser("id2", "login2", "name2", BigDecimal.valueOf(2000), new Date());

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		userService = new UserService(userRepository);
	}

	@Test
	void getUsers() {
		when(userRepository.findById("id1")).thenReturn(Optional.of(user1));

		com.govtech.esm.model.UserDetail uses = userService.getUserDetail("id1");
		verify(userRepository).findById("id1");
	}

	void createUser() {
		when(userRepository.findById("id1")).thenReturn(Optional.of(user1));
	}

}