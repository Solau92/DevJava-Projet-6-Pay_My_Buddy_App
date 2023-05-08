package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationControllerTest {

	private static User loggedUser = new User();
	private static UserDto loggedUserDto = new UserDto();
	private static User user = new User();
	@InjectMocks
	private AuthenticationController authenticationController;
	@Mock
	private UserService userService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private Model model;
	@Mock
	private BindingResult result;

	@BeforeEach
	void setUp() {

		loggedUser.setId(1);
		loggedUser.setFirstname("firstname");
		loggedUser.setLastname("lastname");
		loggedUser.setEmail("email@email.com");
		loggedUser.setPassword("password");
		loggedUser.setAccountBalance(100.00);

		loggedUserDto.setId(1);
		loggedUserDto.setFirstname("firstname");
		loggedUserDto.setLastname("lastname");
		loggedUserDto.setEmail("email@email.com");
		loggedUserDto.setPassword("password");
		loggedUserDto.setAccountBalance(100.00);

		user.setId(2);
		user.setFirstname("firstnameTest");
		user.setLastname("lastnameTest");
		user.setEmail("email@email.com");
		user.setPassword("passwordTest");
		user.setAccountBalance(50.00);
	}

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
		String expected = authenticationController.showRegistrationForm(model);

		// THEN
		assertEquals("register", expected);
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
		when(userService.findUserByEmail(anyString())).thenReturn(null);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		String expected = authenticationController.registration(loggedUserDto, result, model);

		// THEN
		assertEquals("redirect:/index?success", expected);
	}

	@Test
	void registration_EmailAlreadyRegistred_Test() {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(user);

		// WHEN
		String expected = authenticationController.registration(loggedUserDto, result, model);

		// THEN
		assertEquals("register", expected);
	}

	@Test
	void registration_OtherErrors_Test() {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(null);
		when(result.hasErrors()).thenReturn(true);

		// WHEN
		String expected = authenticationController.registration(loggedUserDto, result, model);

		// THEN
		assertEquals("register", expected);
	}

}
