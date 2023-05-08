package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.IncorrectAmountException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.exception.UserNotFoundException;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import com.paymybuddy.paymybuddyapp.service.TransferService;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.mockito.ArgumentMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class TransferControllerTest {

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

	private static Transfer transfer1 = new Transfer();

	private static TransferDto transferDto1 = new TransferDto();
	private static TransferDto transferDto2 = new TransferDto();
	private static List<TransferDto> transfersDone = new ArrayList<>();


	@BeforeEach
	void setUp(){

		loggedUser.setId(1);
		loggedUser.setFirstname("firstnameTest");
		loggedUser.setLastname("lastnameTest");
		loggedUser.setEmail("emailTest@email.com");
		loggedUser.setPassword("passwordTest");
		loggedUser.setAccountBalance(100.5);

		friendUser.setId(1);
		friendUser.setFirstname("friendFirstnameTest");
		friendUser.setLastname("friendLastnameTest");
		friendUser.setEmail("friendEmailTest@email.com");
		friendUser.setPassword("friendPasswordTest");
		friendUser.setAccountBalance(50.00);

		transfer1.setId(1);
		transfer1.setDebtor(2);
		transfer1.setCreditor(1);
		transfer1.setReason("because");
		transfer1.setAmount(200);
		transfer1.setDate(LocalDate.now());

		transferDto1.setDebtor(2);
		transferDto1.setCreditor(1);
		transferDto1.setReason("because");
		transferDto1.setAmount(200);
		transferDto1.setDate(LocalDate.now());
		transferDto1.setCreditorEmail("friendEmailTest@email.com");

		transferDto2.setDebtor(2);
		transferDto2.setCreditor(1);
		transferDto2.setReason("becauseAgain");
		transferDto2.setAmount(50);
		transferDto2.setDate(LocalDate.now());
		transferDto2.setCreditorEmail("friendEmailTest@email.com");

		transfersDone.add(transferDto1);
		transfersDone.add(transferDto2);

	}

	@Test
	void getTransfer_Ok_Test() throws UserNotFoundException, ContactNotFoundException {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);
		when(transferService.findAllUsersTransfers(ArgumentMatchers.any(User.class))).thenReturn(transfersDone);

		// WHEN
		String expected = transferController.transfer(model);

		// THEN
		assertEquals("transfer", expected);
		assertEquals(null, transferController.getMessage());

	}

	@Test
	void getTransfer_LoggedUserNull_Test() throws UserNotFoundException, ContactNotFoundException {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(null);

		// WHEN
		String expected = transferController.transfer(model);

		// THEN
		assertEquals("redirect:/user/transfer?error", expected);
		assertEquals("Logged user not found", transferController.getMessage());
	}

	@Test
	void addTransfer_Ok_Test() throws InsufficientBalanceException {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);
		when(transferRepository.save(ArgumentMatchers.any(Transfer.class))).thenReturn(transfer1);
		when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(loggedUser).thenReturn(friendUser);

		// WHEN
		transferController.addTransfer(transferDto1, result, model);

		// THEN
		assertEquals("The transfer was successfully done ! You have now 100.5 € on your account", transferController.getMessage());
	}

	@Test
	void addTransfer_LoggedUserNull_Test() throws InsufficientBalanceException {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(null);

		// WHEN
		transferController.addTransfer(transferDto1, result, model);

		// THEN
		assertEquals("Logged user not found, the transfer was not effected", transferController.getMessage());
	}

	@Test
	void addTransfer_InsufficientBalanceException_Test() throws InsufficientBalanceException, Exception {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);
		doThrow(InsufficientBalanceException.class).when(transferService).saveTransfer(any(User.class), any(TransferDto.class));

		// WHEN
		transferController.addTransfer(transferDto1, result, model);

		// THEN
		assertEquals("Your balance account is insufficient, the transfer was not effected. You can send a maximum of 100.0 €", transferController.getMessage());
	}

	@Test
	void addTransfer_AmountEqualsZero_Test() throws InsufficientBalanceException, Exception {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);
		doThrow(IncorrectAmountException.class).when(transferService).saveTransfer(any(User.class), any(TransferDto.class));

		// WHEN
		transferController.addTransfer(transferDto1, result, model);

		// THEN
		assertEquals("Amount equals zero, the transfer was not effected", transferController.getMessage());
	}

	@Test
	void addTransfer_NoContactSelected_Test() throws InsufficientBalanceException, Exception {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);
		doThrow(ContactNotFoundException.class).when(transferService).saveTransfer(any(User.class), any(TransferDto.class));

		// WHEN
		transferController.addTransfer(transferDto1, result, model);

		// THEN
		assertEquals("No contact selected, the transfer was not effected", transferController.getMessage());
	}

	@Test
	void addTransfer_OtherException_Test() throws Exception {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);
		doThrow(Exception.class).when(transferService).saveTransfer(any(User.class), any(TransferDto.class));

		// WHEN
		transferController.addTransfer(transferDto1, result, model);

		// THEN
		assertEquals("Unknown error, the transfer was not effected", transferController.getMessage());
	}

}
