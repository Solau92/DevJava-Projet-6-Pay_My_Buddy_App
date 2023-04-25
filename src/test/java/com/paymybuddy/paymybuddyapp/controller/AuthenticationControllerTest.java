package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.service.TransferService;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationControllerTest {

	@InjectMocks
	private AuthenticationController authenticationController;

	@Mock
	private UserService userService;

	@Mock
	private TransferService transferService;

	@Mock
	private Model model;

	@Mock
	private BindingResult result;

	@Test
	void getIndex_Ok_Test() {

		// GIVEN
		// WHEN
		String expected = authenticationController.index();

		// THEN
		assertEquals("index", expected);

	}

	@Test
	void showRegistrationForm_Ok_Test() {

		// GIVEN

		// WHEN

		// THEN

		fail("Not yet implemented");
	}

	@Test
	void getLogin_Ok_Test() {

		// GIVEN
		// WHEN
		String expected = authenticationController.login();

		// THEN
		assertEquals("login", expected);
	}

	@Test
	void getLogoff_Ok_Test() {

		// GIVEN
		// WHEN
		String expected = authenticationController.logoff();

		// THEN
		assertEquals("logoff", expected);
	}

	@Test
	void registration_Ok_Test() {

		// GIVEN

		// WHEN

		// THEN

		fail("Not yet implemented");
	}

}
