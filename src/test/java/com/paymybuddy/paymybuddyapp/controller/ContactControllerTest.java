package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import com.paymybuddy.paymybuddyapp.service.ContactService;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContactControllerTest {

	@InjectMocks
	private ContactController contactController;

	@Mock
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ContactService contactService;

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
	void getContact_Ok_Test() {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);

		// WHEN
		String expected = contactController.contact(model);

		// THEN
		assertEquals("contact", expected);
		assertNull(contactController.getMessage());
	}

	@Test
	void addContact_Ok_Test() throws Exception {

		// GIVEN
		when(contactService.isContactValid(anyString())).thenReturn(true);
		when(userRepository.findByEmail(anyString())).thenReturn(loggedUser);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		contactController.addContact("email", result, model);

		// THEN
		assertEquals("Your friend was successfully added !", contactController.getMessage());

	}

	@Test
	void addContact_LoggedUserException_Test() throws Exception {

		// GIVEN
		when(contactService.isContactValid(anyString())).thenThrow(LoggedUserException.class);

		// WHEN
		contactController.addContact("email", result, model);

		// THEN
		assertEquals("You can't add yourself to your list of contacts", contactController.getMessage());
	}

	@Test
	void addContact_ContactNotFoundException_Test() throws Exception {

		// GIVEN
		when(contactService.isContactValid(anyString())).thenThrow(ContactNotFoundException.class);

		// WHEN
		contactController.addContact("email", result, model);

		// THEN
		assertEquals("This contact was not found", contactController.getMessage());

	}

	@Test
	void addContact_ContactAlreadyExistsException_Test() throws Exception {

		// GIVEN
		when(contactService.isContactValid(anyString())).thenThrow(ContactAlreadyExistsException.class);

		// WHEN
		contactController.addContact("email", result, model);

		// THEN
		assertEquals("This contact is already in your list", contactController.getMessage());
	}

	@Test
	void addContact_OtherException_Test() throws Exception {

		// GIVEN
		when(contactService.isContactValid(anyString())).thenThrow(Exception.class);

		// WHEN
		contactController.addContact("email", result, model);

		// THEN
		assertEquals("Error, try again", contactController.getMessage());

	}

}
