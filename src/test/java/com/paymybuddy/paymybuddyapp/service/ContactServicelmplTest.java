package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContactServicelmplTest {

	@InjectMocks
	private ContactServiceImpl contactService;

	@Mock
	private UserService userService;

	private User loggedUser = new User();

	private User friend = new User();

	@BeforeEach
	void setUp() {

		loggedUser.setId(1);
		loggedUser.setFirstname("firstnameTest");
		loggedUser.setLastname("lastnameTest");
		loggedUser.setEmail("emailTest@email.com");
		loggedUser.setPassword("passwordTest");
		loggedUser.setAccountBalance(105.00);

		friend.setId(2);
		friend.setFirstname("firstnameFriendTest");
		friend.setLastname("lastnameFriendTest");
		friend.setEmail("emailFriendTest@email.com");
		friend.setPassword("passwordFriendTest");
		friend.setAccountBalance(210.00);
	}

	@Test
	void isContactValid_Yes_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser).thenReturn(friend);

		// WHEN
		Boolean expected = contactService.isContactValid(loggedUser, friend);

		// THEN
		assertTrue(expected);
	}

	@Test
	void isContactValid_LoggerUserException_Test() {

		// GIVEN
		// WHEN
		// THEN
		assertThrows(LoggedUserException.class, ()-> contactService.isContactValid(loggedUser, loggedUser));
	}

	@Test
	void isContactValid_ContactNotFoundException_Test() {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser).thenReturn(null);

		// WHEN
		// THEN
		assertThrows(ContactNotFoundException.class, ()-> contactService.isContactValid(loggedUser, null));
	}

	@Test
	void isContactValid_ContactAlreadyExistsException_Test() {

		// GIVEN
		loggedUser.getContacts().add(friend);

		// WHEN
		// THEN
		assertThrows(ContactAlreadyExistsException.class, ()-> contactService.isContactValid(loggedUser, friend));
	}

}
