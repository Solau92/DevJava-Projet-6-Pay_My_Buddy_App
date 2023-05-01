package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.IncorrectAmountException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransferServiceImplTest {

	@InjectMocks
	private TransferServiceImpl transferService;

	@Mock
	private TransferRepository transferRepository;

	@Mock
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Captor
	ArgumentCaptor<Transfer> transferCaptor;

	@Captor
	ArgumentCaptor<TransferDto> transferDtoCaptor;

	private User loggedUser = new User();
	private User friendUser = new User();

	private TransferDto transferDto1 = new TransferDto();

	private Transfer transfer1 = new Transfer();

	private TransferDto transferDto2 = new TransferDto();

	private TransferDto transferDto3 = new TransferDto();

	private TransferDto transferDto4 = new TransferDto();


	private Transfer transfer2 = new Transfer();


	@BeforeEach
	void setUp() {

		loggedUser.setId(1);
		loggedUser.setFirstname("firstnameTest");
		loggedUser.setLastname("lastnameTest");
		loggedUser.setEmail("emailTest@email.com");
		loggedUser.setPassword("passwordTest");
		loggedUser.setAccountBalance(105.00);

		friendUser.setId(2);
		friendUser.setFirstname("firstnameFriendTest");
		friendUser.setLastname("lastnameFriendTest");
		friendUser.setEmail("emailFriendTest@email.com");
		friendUser.setPassword("passwordFriendTest");
		friendUser.setAccountBalance(210.00);

		transfer1.setId(1);
		transfer1.setDebtor(1);
		transfer1.setCreditor(2);
		transfer1.setReason("because");
		transfer1.setAmount(100);
		transfer1.setDate(LocalDate.now());

		transferDto1.setDebtor(1);
		transferDto1.setCreditor(2);
		transferDto1.setReason("because");
		transferDto1.setAmount(100);
		transferDto1.setDate(LocalDate.now());
		transferDto1.setCreditorEmail("friendEmailTest@email.com");

		transfer2.setId(2);
		transfer2.setDebtor(1);
		transfer2.setCreditor(2);
		transfer2.setReason("becauseAgain");
		transfer2.setAmount(0);
		transfer2.setDate(LocalDate.now());

		transferDto2.setDebtor(1);
		transferDto2.setCreditor(2);
		transferDto2.setReason("becauseAgain");
		transferDto2.setAmount(0);
		transferDto2.setDate(LocalDate.now());
		transferDto2.setCreditorEmail("friendEmailTest@email.com");

		transferDto3.setDebtor(1);
		transferDto3.setCreditor(2);
		transferDto3.setReason("becauseAgain");
		transferDto3.setAmount(1000);
		transferDto3.setDate(LocalDate.now());
		transferDto3.setCreditorEmail("friendEmailTest@email.com");

		transferDto4.setDebtor(1);
		transferDto4.setCreditor(2);
		transferDto4.setReason("becauseAgain");
		transferDto4.setAmount(-1000);
		transferDto4.setDate(LocalDate.now());
		transferDto4.setCreditorEmail("friendEmailTest@email.com");
	}

	@Test
	void saveTransfer_Ok_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser).thenReturn(friendUser);
		when(transferRepository.save(any(Transfer.class))).thenReturn(transfer1);


		// WHEN
		Transfer savedTransfer = transferService.saveTransfer(loggedUser, transferDto1);

		// THEN
		verify(transferRepository, Mockito.times(1)).save(any(Transfer.class));
		verify(userService, Mockito.times(1)).addTransfer(transferDtoCaptor.capture());
		assertEquals(transfer1.getAmount(), savedTransfer.getAmount());
		TransferDto transferDtoSaved = transferDtoCaptor.getValue();
		assertEquals(100, transferDtoSaved.getAmount());
	}

	@Test
	void saveTransfer_AccountBalanceNotSufficient_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser).thenReturn(friendUser);

		// WHEN
		// THEN
		assertThrows(InsufficientBalanceException.class, ()-> transferService.saveTransfer(loggedUser, transferDto3));
	}


	@Test
	void saveTransfer_AmountZero_Test() throws IncorrectAmountException {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);

		// WHEN
		// THEN
		assertThrows(IncorrectAmountException.class, ()-> transferService.saveTransfer(loggedUser, transferDto2));

	}

	@Test
	void saveTransfer_AmountNegative_Test() throws IncorrectAmountException {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);

		// WHEN
		// THEN
		assertThrows(IncorrectAmountException.class, ()-> transferService.saveTransfer(loggedUser, transferDto4));

	}

/*	@Test
	void findAllUsersTransfers_Ok_Test() throws Exception {

		// GIVEN

		// WHEN

		// THEN

		fail("not yet implemented");
	}*/

}
