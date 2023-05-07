
package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ContactIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	UserService userService;

	@Autowired
	private WebApplicationContext context;


	@BeforeEach
	public void setUp(){
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}


	/////////////////////////////////////////////////////////////////

	@Test
	@WithMockUser
	void addContact_IT() throws Exception {

		mockMvc.perform(post("/user/contact/save")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//						.with(user("adalovelace@gmail.com"))
						.param("email", "hedyLamarr@gmail.com")
		)
				.andDo(print())
				.andExpect(view().name("redirect:/user/contact?success"));


	User loggedUser = new User();
		// Comment je "trouve" mon loggedUser ?

		User friend = userService.findUserByEmail("billGates@email.com");

		// GIVEN
		// WHEN
		// Requête post page contact avec l'email friend

		User loggedUserInDataBase = userService.findUserByEmail(loggedUser.getEmail());
		List<User> contacts = loggedUserInDataBase.getContacts();

		// THEN
		assertTrue(contacts.contains(friend));

		fail("TODO");


	}

	public static RequestPostProcessor loggedUser(){
		return user("adalovelace@gmail.com");
	}
	/////////////////////////////////////////////////////////////////


}

