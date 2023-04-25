package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class ProfileControllerTest {

	@InjectMocks
	private ProfileController profileController;

	@Mock
	private UserService userService;

	@Mock
	private Model model;

	@Test
	void getProfile_Ok_Test(){

		// GIVEN

		// WHEN
		profileController.profile(model);

		// THEN

	}

}
