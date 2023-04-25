package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import com.paymybuddy.paymybuddyapp.service.TransferService;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.mockito.ArgumentMatchers;

import java.sql.SQLException;
import java.time.LocalDate;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferControllerTest {


	@InjectMocks
	private TransferController transferController;
	@Mock
	private UserService userService;
	@Mock
	private TransferService transferService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private TransferRepository transferRepository;

	@Mock
	private Model model;
	@Mock
	private BindingResult result;

	private static User loggedUser = new User();

	private static User friendUser = new User();

	private static Transfer transfer = new Transfer();

	private static TransferDto transferDto = new TransferDto();


	@BeforeEach
	void setUp(){

		loggedUser.setId(1);
		loggedUser.setFirstname("firstnameTest");
		loggedUser.setLastname("lastnameTest");
		loggedUser.setEmail("emailTest@email.com");
		loggedUser.setPassword("passwordTest");
		loggedUser.setAccountBalance(105.00);

		friendUser.setId(1);
		friendUser.setFirstname("friendFirstnameTest");
		friendUser.setLastname("friendLastnameTest");
		friendUser.setEmail("friendEmailTest@email.com");
		friendUser.setPassword("friendPasswordTest");
		friendUser.setAccountBalance(50.00);

		transfer.setId(1);
		transfer.setDebtor(2);
		transfer.setCreditor(1);
		transfer.setReason("because");
		transfer.setAmount(200);
		transfer.setDate(LocalDate.now());

		transferDto.setDebtor(2);
		transferDto.setCreditor(1);
		transferDto.setReason("because");
		transferDto.setAmount(200);
		transferDto.setDate(LocalDate.now());
		transferDto.setCreditorEmail("friendEmailTest@email.com");
	}

	@Test
	void getTransfer_Ok_Test() {

		// GIVEN

		// WHEN

		// THEN
		fail("not yet implemented");
	}

	@Test
	void addTransfer_Ok_Test() throws InsufficientBalanceException {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(transferService.isAccountBalanceSufficient(ArgumentMatchers.any(TransferDto.class), anyDouble())).thenReturn(true);
		when(transferRepository.save(ArgumentMatchers.any(Transfer.class))).thenReturn(transfer);
		when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(loggedUser).thenReturn(friendUser);

		// WHEN
		transferController.addTransfer(transferDto, result, model);

		// THEN
		assertEquals("The transfer was successfully done ! You have now 105.0 € on your account", transferController.getMessage());
	}

	@Test
	void addTransfer_LoggedUserNull_Test() throws InsufficientBalanceException {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(null);

		// WHEN
		transferController.addTransfer(transferDto, result, model);

		// THEN
		assertEquals("Logged user not found, the transfer was not effected", transferController.getMessage());
	}

	@Test
	void addTransfer_InsufficientBalanceException_Test() throws InsufficientBalanceException {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(transferService.isAccountBalanceSufficient(ArgumentMatchers.any(TransferDto.class), anyDouble())).thenThrow(InsufficientBalanceException.class);

		// WHEN
		transferController.addTransfer(transferDto, result, model);

		// THEN
		assertEquals("Your balance account is insufficient, the transfer was not effected. You can send a maximum of 100.0 €", transferController.getMessage());
	}

	@Test
	void addTransfer_OtherException_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(transferService.isAccountBalanceSufficient(ArgumentMatchers.any(TransferDto.class), anyDouble())).thenThrow(Exception.class);

		// WHEN
		transferController.addTransfer(transferDto, result, model);

		// THEN
		assertEquals("Unknown error, the transfer was not effected", transferController.getMessage());
	}

}
