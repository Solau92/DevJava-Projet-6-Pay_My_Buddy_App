package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class TransferIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	UserService userService;

	/////////////////////////////////////////////////////////////////

	@Test
	void addTransfer_IT(){

		User loggedUser = new User();
		// Comment je "trouve" mon loggedUser ?

		User friend = userService.findUserByEmail("billGates@email.com");

		double loggedUserInitialAccountBalance = loggedUser.getAccountBalance();
		double friendInitialAccountBalance = friend.getAccountBalance();
		double transferAmount = 100;

		// GIVEN
		// WHEN
		// Requête post page transfer avec les différentes infos requises

		User loggedUserInDataBase = userService.findUserByEmail(loggedUser.getEmail());
		User friendInDataBase = userService.findUserByEmail(friend.getEmail());

		// THEN
		assertEquals(loggedUserInitialAccountBalance - transferAmount, loggedUserInDataBase.getAccountBalance());
		assertEquals(friendInitialAccountBalance + transferAmount, friendInDataBase.getAccountBalance());

	}

	/////////////////////////////////////////////////////////////////

}
