
package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransferIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;
	@Autowired
	TransferRepository transferRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	private String loggedUserEmail;

	private String friendUserEmail;

	@BeforeEach
	void setUpEach() throws Exception {

		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();

		// I clean the database
		transferRepository.deleteAll();
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

		// I add money on the loggedUser account

		double addedAmount = 1000;

		mockMvc.perform(post("/user/home/addmoney")
						.with(user(loggedUserEmail))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("amount", String.valueOf(addedAmount))
				)
				.andDo(print())
				.andExpect(redirectedUrl("/user/home?successAdd"));
	}

	@Test
	void addTransfer_IT() throws Exception {

		// I get the User and the friendUser from database
		User loggedUser = userService.findUserByEmail(loggedUserEmail);
		User friendUser = userService.findUserByEmail(friendUserEmail);

		double initialLoggedUserAccountAmount = loggedUser.getAccountBalance();
		double initialFriendUserAccountAmount = friendUser.getAccountBalance();

		double transferAmount = 100;

		// I make a transfer
		mockMvc.perform(post("/user/transfer/pay")
						.with(user(loggedUserEmail))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("creditorEmail", friendUserEmail)
						.param("amount", String.valueOf(transferAmount))
						.param("reason", "because I o u 100")
				)
				.andDo(print())
				.andExpect(view().name("redirect:/user/transfer?success"));

		// I get the User and friend saved from database
		User loggedUserInDataBase = userService.findUserByEmail(loggedUserEmail);
		User friendUserInDataBase = userService.findUserByEmail(friendUserEmail);

		// I check that the account balance of
		assertEquals(initialFriendUserAccountAmount + transferAmount, friendUserInDataBase.getAccountBalance());
		assertEquals(initialLoggedUserAccountAmount - transferAmount*1.005, loggedUserInDataBase.getAccountBalance());

	}
}

