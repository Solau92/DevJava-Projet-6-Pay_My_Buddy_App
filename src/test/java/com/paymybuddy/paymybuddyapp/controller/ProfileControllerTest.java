package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProfileControllerTest {

	@InjectMocks
	private ProfileController profileController;

	@Mock
	private UserService userService;

	@Mock
	private Model model;

	private static User loggedUser = new User();

	@BeforeEach
	void setUp(){
		loggedUser.setId(1);
		loggedUser.setFirstname("firstnameTest");
		loggedUser.setLastname("lastnameTest");
		loggedUser.setEmail("emailTest@email.com");
		loggedUser.setPassword("passwordTest");
		loggedUser.setAccountBalance(100.00);
	}

	@Test
	void getProfile_Ok_Test(){

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);

		// WHEN
		profileController.profile(model);
		String expected = profileController.profile(model);

		// THEN
		assertEquals(null, profileController.getMessage());
		assertEquals("profile", expected);
	}

	@Test
	void getProfile_LoggedUserNull_Test(){

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(null);

		// WHEN
		profileController.profile(model);
		String expected = profileController.profile(model);

		// THEN
		assertEquals("Logged user not found", profileController.getMessage());
		assertEquals("redirect:/user/profile?error", expected);
	}

}
