package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
	void addTransfer_IT() throws Exception {

		mockMvc.perform(post("/user/transfer/pay")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)
								.with(loggedUser())
								.param("amount", String.valueOf(100))
								.param("reason", "becoz")
								.param("creditor", String.valueOf(2))
								.param("debtor", String.valueOf(1))
				)
				.andDo(print())
				.andExpect(view().name("redirect:/user/transfer?success"));


















	/*	User loggedUser = userService.findUserByEmail("billGates@email.com");

		loggedUser.printUser();

		User friend = userService.findUserByEmail("billGates@email.com");

		friend.printUser();

//		double loggedUserInitialAccountBalance = loggedUser.getAccountBalance();
		double friendInitialAccountBalance = friend.getAccountBalance();
		double transferAmount = 100;

		TransferDto transferToSave = new TransferDto();
		transferToSave.setAmount(transferAmount);
		transferToSave.setReason("because i o u 100");
		transferToSave.setCreditor(friend.getId());
//		transferToSave.setDebtor(loggedUser.getId());

		// GIVEN
		// WHEN
		mockMvc.perform(post("/user/transfer/pay")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.with(loggedUser())
						.param("amount", String.valueOf(transferAmount))
						.param("reason", transferToSave.getReason())
						.param("creditor", String.valueOf(friend.getId()))
//						.param("debtor", String.valueOf(loggedUser.getId()))
				)
				.andDo(print())
				.andExpect(view().name("redirect:/user/transfer?success"));

//		User loggedUserInDataBase = userService.findUserByEmail(loggedUser.getEmail());
		User friendInDataBase = userService.findUserByEmail(friend.getEmail());

		// THEN
//		assertEquals(loggedUserInitialAccountBalance - transferAmount, loggedUserInDataBase.getAccountBalance());
		assertEquals(friendInitialAccountBalance + transferAmount, friendInDataBase.getAccountBalance());
*/
	}

	/////////////////////////////////////////////////////////////////

	public static RequestPostProcessor loggedUser(){
		return user("adalovelace@gmail.com");
	}
}
