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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ContactControllerTest {

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

	private User loggedUser = new User();

	private User friend = new User();

	@BeforeEach
	void setUp(){

		loggedUser.setId(1);
		loggedUser.setFirstname("firstnameTest");
		loggedUser.setLastname("lastnameTest");
		loggedUser.setEmail("emailTest@email.com");
		loggedUser.setPassword("passwordTest");
		loggedUser.setAccountBalance(100.00);

		friend.setId(2);
		friend.setFirstname("firstnameFriendTest");
		friend.setLastname("lastnameFriendTest");
		friend.setEmail("emailFriendTest@email.com");
		friend.setPassword("passwordFriendTest");
		friend.setAccountBalance(210.00);
	}

	@Test
	void getContact_Ok_Test() {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);

		// WHEN
		String expected = contactController.contact(model);

		// THEN
		assertEquals("contact", expected);
		assertNull(contactController.getMessage());
	}

	@Test
	void addContact_Ok_Test() throws Exception {

		// GIVEN
		when(userRepository.findByEmail(anyString())).thenReturn(loggedUser).thenReturn(friend);
		when(contactService.isContactValid(any(User.class), any(User.class))).thenReturn(true);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		contactController.addContact("email", result, model);

		// THEN
		assertEquals("Your friend was successfully added !", contactController.getMessage());

	}

	@Test
	void addContact_LoggedUserException_Test() throws Exception {

		// GIVEN
		doThrow(LoggedUserException.class).when(userService).addContact(anyString());

		// WHEN
		contactController.addContact("emailTest@email.com", result, model);

		// THEN
		assertEquals("You can't add yourself to your list of contacts", contactController.getMessage());
	}

	@Test
	void addContact_ContactNotFoundException_Test() throws Exception {

		// GIVEN
		doThrow(ContactNotFoundException.class).when(userService).addContact(anyString());

		// WHEN
		contactController.addContact("email", result, model);

		// THEN
		assertEquals("This contact was not found", contactController.getMessage());

	}

	@Test
	void addContact_ContactAlreadyExistsException_Test() throws Exception {

		// GIVEN
		doThrow(ContactAlreadyExistsException.class).when(userService).addContact(anyString());

		// WHEN
		contactController.addContact("emailFriendTest@email.com", result, model);

		// THEN
		assertEquals("This contact is already in your list", contactController.getMessage());
	}

	@Test
	void addContact_OtherException_Test() throws Exception {

		// GIVEN
		doThrow(Exception.class).when(userService).addContact(anyString());

		// WHEN
		contactController.addContact("email", result, model);

		// THEN
		assertEquals("Error, try again", contactController.getMessage());
	}

}
