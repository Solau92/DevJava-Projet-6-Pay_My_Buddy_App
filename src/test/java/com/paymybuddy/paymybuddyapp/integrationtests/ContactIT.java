
package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContactIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private WebApplicationContext context;

	private String loggedUserEmail;

	private String friendUserEmail;

	@BeforeEach
	void setUpEach() throws Exception {

		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();

		userRepository.deleteAll();

		// I create of a new User
		loggedUserEmail = "email@email.com";
		UserDto loggedUserToSave = new UserDto();
		loggedUserToSave.setFirstname("firstname");
		loggedUserToSave.setLastname("lastname");
		loggedUserToSave.setEmail(loggedUserEmail);
		loggedUserToSave.setPassword("1234");

		// I save this User in database
		mockMvc.perform(post("/register/save")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstname", loggedUserToSave.getFirstname())
				.param("lastname", loggedUserToSave.getLastname())
				.param("email", loggedUserToSave.getEmail())
				.param("password", loggedUserToSave.getPassword())
				.with(csrf())
		);

		// I create of a second User
		friendUserEmail = "friendemail@email.com";
		UserDto friendUserToSave = new UserDto();
		friendUserToSave.setFirstname("friendfirstname");
		friendUserToSave.setLastname("friendlastname");
		friendUserToSave.setEmail(friendUserEmail);
		friendUserToSave.setPassword("1234");

		// I save this User in database
		mockMvc.perform(post("/register/save")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstname", friendUserToSave.getFirstname())
				.param("lastname", friendUserToSave.getLastname())
				.param("email", friendUserToSave.getEmail())
				.param("password", friendUserToSave.getPassword())
				.with(csrf())
		);

	}

	@Test
	void addContact_IT() throws Exception {

		// I get the User and the friendUser from database
		User loggedUser = userService.findUserByEmail(loggedUserEmail);

		User friendUser = userService.findUserByEmail(friendUserEmail);

		// I add the friend to the contact's loggedUser list
		mockMvc.perform(post("/user/contact/save")
						.with(user(loggedUser.getEmail()))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("email", friendUser.getEmail())
				)
				.andDo(print())
				.andExpect(redirectedUrl("/user/contact?success"));
	}

}

