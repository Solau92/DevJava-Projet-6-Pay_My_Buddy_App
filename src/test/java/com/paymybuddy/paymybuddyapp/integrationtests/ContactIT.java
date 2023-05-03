package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ContactIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	UserService userService;


	/////////////////////////////////////////////////////////////////

	@Test
	void addContact_IT() {

/*		User loggedUser = new User();
		// Comment je "trouve" mon loggedUser ?

		User friend = userService.findUserByEmail("billGates@email.com");

		// GIVEN
		// WHEN
		// Requête post page contact avec l'email friend

		User loggedUserInDataBase = userService.findUserByEmail(loggedUser.getEmail());
		List<User> contacts = loggedUserInDataBase.getContacts();

		// THEN
		assertTrue(contacts.contains(friend));*/

	}

	/////////////////////////////////////////////////////////////////


}
