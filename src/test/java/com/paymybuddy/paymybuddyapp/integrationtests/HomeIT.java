/*
package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.controller.HomeController;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user@gmail.com", password = "1234")
class HomeIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@BeforeEach
	void setUp(){
		mockMvc = MockMvcBuilders.standaloneSetup(new HomeController(userService)).build();
	}


	/////////////////////////////////////////////////////////////////

	@Test
	void addMoney_IT(){

		User loggedUser = new User();
		// Comment je "trouve" mon loggedUser ?
		double initialAmount = loggedUser.getAccountBalance();

		// GIVEN
		// WHEN
		// Requête post page home avec le montant

		double addedAmount = 100;

		User loggedUserInDataBase = userService.findUserByEmail(loggedUser.getEmail());

		// THEN
		assertEquals(initialAmount + addedAmount, loggedUserInDataBase.getAccountBalance());

		fail("TODO");

	}

	@Test
	void withdrawMoney_IT(){

		User loggedUser = new User();
		// Comment je "trouve" mon loggedUser ?
		double initialAmount = loggedUser.getAccountBalance();

		// GIVEN
		// WHEN
		// Requête post page home avec le montant
		double withdrawnAmount = 100;

		// WHEN
		User loggedUserInDataBase = userService.findUserByEmail(loggedUser.getEmail());

		// THEN
		assertEquals(initialAmount - withdrawnAmount, loggedUserInDataBase.getAccountBalance());

		fail("TODO");

	}

	/////////////////////////////////////////////////////////////////


//	@Test
//	@WithMockUser(username = "user@gmail.com", password = "1234")
//	void getHomeTest() throws Exception {
//
//		mockMvc.perform(get("/user/home"))
//				.andExpect(status().isAccepted());
//
//		mockMvc.perform(get("/user/home"))
//				.andDo(print())
//				.andExpect(view().name("home"));
//
//				mockMvc.perform(get("/user/home"))
//						.with((user("user@email.com").password("$2a$10$CoSxVq2dlWbv7PgVF.knnunHxzrxK2eCQJTwHarTNv9eruob1lAj."))))
//						.andExpect(view().name("home"));
//	}


}
*/
