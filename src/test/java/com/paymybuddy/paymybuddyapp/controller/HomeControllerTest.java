package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.IncorrectAmountException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class HomeControllerTest {

	@InjectMocks
	private HomeController homeController;

	@Mock
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Model model;

	@Mock
	private BindingResult result;

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
	void getHome_Ok_Test() {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);

		// WHEN
		homeController.home(model);
		String expected = homeController.home(model);

		// THEN
		assertEquals(null, homeController.getMessage());
		assertEquals("home", expected);

	}


	@Test
	void getHome_LoggedUserNull_Test() {

/*		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(null);

		// WHEN
		homeController.home(model);
		String expected = homeController.home(model);

		// THEN
		assertEquals("Logged user not found", homeController.getMessage());
		assertEquals("redirect:/user/transfer?error", expected);*/

	}

	@Test
	void addMoney_Ok_Test(){

		// GIVEN
		when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(loggedUser);

		// WHEN
		homeController.addMoney(10, result, model);

		// THEN
		assertEquals(100, loggedUser.getAccountBalance());
		assertEquals("Money was successfully added to your account", homeController.getMessage());

	}

	@Test
	void addMoney_AmountZeroException_Test() throws Exception {

		// GIVEN
		doThrow(IncorrectAmountException.class).when(userService).addMoney(anyDouble());

		// WHEN
		homeController.addMoney(0, result, model);

		// THEN
		assertEquals("Error, the amount cannot be equal to 0 €", homeController.getMessage());

	}

	@Test
	void addMoney_OtherException_Test() throws Exception {

		// GIVEN
		doThrow(Exception.class).when(userService).addMoney(anyDouble());

		// WHEN
		homeController.addMoney(0, result, model);

		// THEN
		assertEquals("Error", homeController.getMessage());
	}

	@Test
	void withdrawMoney_Ok_Test(){

		// GIVEN
		when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(loggedUser);

		// WHEN
		homeController.withdrawMoney(10, model);

		// THEN
		assertEquals(100, loggedUser.getAccountBalance());
		assertEquals("Money was sent to your bank account", homeController.getMessage());

	}

	@Test
	void withdrawMoney_InsufficientBalanceException_Test() throws Exception {

		// GIVEN
		doThrow(InsufficientBalanceException.class).when(userService).withdrawMoney(anyDouble());

		// WHEN
		homeController.withdrawMoney(0, model);

		// THEN
		assertEquals("You can't withdraw more than the amount of your account balance", homeController.getMessage());

	}

	@Test
	void withdrawMoney_OtherException_Test() throws Exception {

		// GIVEN
		doThrow(Exception.class).when(userService).withdrawMoney(anyDouble());

		// WHEN
		homeController.withdrawMoney(0, model);

		// THEN
		assertEquals("Error", homeController.getMessage());

	}

}
