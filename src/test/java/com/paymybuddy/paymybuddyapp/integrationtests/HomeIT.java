
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HomeIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	private String loggedUserEmail;

	@BeforeEach
	void setUpEach() throws Exception {

		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();

		// I clean the database
		userRepository.deleteAll();

		// I create of a new User
		loggedUserEmail = "email@email.com";
		UserDto userToSave = new UserDto();
		userToSave.setFirstname("firstname");
		userToSave.setLastname("lastname");
		userToSave.setEmail(loggedUserEmail);
		userToSave.setPassword("1234");

		// I save this User in database
		mockMvc.perform(post("/register/save")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("firstname", userToSave.getFirstname())
						.param("lastname", userToSave.getLastname())
						.param("email", userToSave.getEmail())
						.param("password", userToSave.getPassword())
						.with(csrf())
				);
	}

	@Test
	void addMoney_IT() throws Exception {

		// I get the User saved from database
		User loggedUser = userService.findUserByEmail(loggedUserEmail);

		double initialAmount = loggedUser.getAccountBalance();

		// I add money in the User account
		double addedAmount = 100;

		mockMvc.perform(post("/user/home/addmoney")
						.with(user(loggedUser.getEmail()))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("amount", String.valueOf(addedAmount))
				)
				.andDo(print())
				.andExpect(redirectedUrl("/user/home?successAdd"));

		// I get the User saved from database
		User loggedUserInDataBase = userService.findUserByEmail(loggedUser.getEmail());

		// I check that the account amount is equal to the initial amount + the added amount
		assertEquals(initialAmount + addedAmount, loggedUserInDataBase.getAccountBalance());
	}

	@Test
	void withdrawMoney_IT() throws Exception {

		// I get the User saved from database
		User loggedUser = userService.findUserByEmail(loggedUserEmail);

		double initialAmount = loggedUser.getAccountBalance();

		// I add money in the User account
		double addedAmount = 100;

		mockMvc.perform(post("/user/home/addmoney")
						.with(user(loggedUser.getEmail()))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("amount", String.valueOf(addedAmount))
				)
				.andDo(print())
				.andExpect(redirectedUrl("/user/home?successAdd"));

		// I withdraw money from the User account
		double withdrawnAmount = 50;

		mockMvc.perform(post("/user/home/withdrawmoney")
						.with(user(loggedUser.getEmail()))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("amountWithdrawn", String.valueOf(withdrawnAmount))
				)
				.andDo(print())
				.andExpect(redirectedUrl("/user/home?successWithdraw"));

		// I get the User saved from database
		User loggedUserInDataBase = userService.findUserByEmail(loggedUser.getEmail());

		// I check that the account amount is equal to the initial amount + the added amount - the withdrawn amount
		assertEquals(initialAmount + addedAmount - withdrawnAmount, loggedUserInDataBase.getAccountBalance());
	}

}
