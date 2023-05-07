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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationIT {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	void setUpEach() {

		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();

		// I clean the database
		userRepository.deleteAll();
	}

	@Test
	void register_IT() throws Exception {

		// I create of a new User
		UserDto userToSave = new UserDto();
		userToSave.setFirstname("firstname");
		userToSave.setLastname("lastname");
		userToSave.setEmail("email@email.com");
		userToSave.setPassword("1234");

		// I save this User in database
		mockMvc.perform(post("/register/save")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("firstname", userToSave.getFirstname())
						.param("lastname", userToSave.getLastname())
						.param("email", userToSave.getEmail())
						.param("password", userToSave.getPassword())
						.with(csrf())
				)
				.andDo(print())
				.andExpect(view().name("redirect:/index?success"));

		// I get the User saved from database
		User userSaved = userService.findUserByEmail(userToSave.getEmail());

		// I check that the User's details are ok
		assertEquals(userToSave.getFirstname(), userSaved.getFirstname());
		assertEquals(userToSave.getLastname(), userSaved.getLastname());
		assertEquals(userToSave.getEmail(), userSaved.getEmail());
		assertEquals(0, userSaved.getAccountBalance());

		// I create of a second User
		UserDto userToSave2 = new UserDto();
		userToSave2.setFirstname("firstname");
		userToSave2.setLastname("lastname");
		userToSave2.setEmail("email2@email.com");
		userToSave2.setPassword("1234");

	}

	@Test
	void login_IT() throws Exception {

		// I create of a new User
		UserDto userToSave = new UserDto();
		userToSave.setFirstname("firstname");
		userToSave.setLastname("lastname");
		userToSave.setEmail("email@email.com");
		userToSave.setPassword("1234");

		// I save this User in database
		mockMvc.perform(post("/register/save")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("firstname", userToSave.getFirstname())
						.param("lastname", userToSave.getLastname())
						.param("email", userToSave.getEmail())
						.param("password", userToSave.getPassword())
						.with(csrf())
				)
				.andDo(print())
				.andExpect(view().name("redirect:/index?success"));

		mockMvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("username", userToSave.getEmail())
						.param("password", userToSave.getPassword())
						.with(csrf())
				)
				.andDo(print()).
				andExpect(redirectedUrl("/user/home"));

	}

}
