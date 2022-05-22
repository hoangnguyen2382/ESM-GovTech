package com.govtech.esm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.govtech.esm.controller.UserController;

@SpringBootTest
class EsmApplicationTests {

	@Autowired
	private UserController userController;

	@Test
	public void contextLoads() throws Exception {
		Assertions.assertNotNull(userController);
	}

}
