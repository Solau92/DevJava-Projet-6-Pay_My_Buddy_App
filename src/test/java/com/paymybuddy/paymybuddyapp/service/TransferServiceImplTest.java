package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.AmountZeroException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferServiceImplTest {

	@InjectMocks
	private TransferServiceImpl transferService;

	@Mock
	private TransferRepository transferRepository;

	@Mock
	private UserService userService;

	private User loggedUser = new User();
	private User friendUser = new User();

	private TransferDto transferDto1 = new TransferDto();

	private Transfer transfer1 = new Transfer();

	private TransferDto transferDto2 = new TransferDto();

	private Transfer transfer2 = new Transfer();


	@BeforeEach
	void setUp() {

		loggedUser.setId(1);
		loggedUser.setFirstname("firstnameTest");
		loggedUser.setLastname("lastnameTest");
		loggedUser.setEmail("emailTest@email.com");
		loggedUser.setPassword("passwordTest");
		loggedUser.setAccountBalance(105.00);

		friendUser.setId(1);
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
		transfer2.setAmount(50);
		transfer2.setDate(LocalDate.now());

		transferDto2.setDebtor(1);
		transferDto2.setCreditor(2);
		transferDto2.setReason("becauseAgain");
		transferDto2.setAmount(50);
		transferDto2.setDate(LocalDate.now());
		transferDto2.setCreditorEmail("friendEmailTest@email.com");

	}

	@Test
	void saveTransfer_Ok_Test() throws AmountZeroException {

		// GIVEN
		when(transferRepository.save(transfer1)).thenReturn(transfer1);

		// WHEN
		transferService.saveTransfer(transferDto1);

		// THEN
		// Problème pour tester car ma méthode saveUser ne renvoie rien...
		fail("not yet implemented");
	}

	@Test
	void findAllUsersTransfers_Ok_Test(){

/*		// GIVEN
		List<Transfer> transfers = new ArrayList<>();
		transfers.add(transfer1);
		transfers.add(transfer2);

		// Comment faire ?
		when(user.getTransfersDone()).thenReturn(transfers);

		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser).thenReturn(friendUser);

		// WHEN
		List<TransferDto> expected = transferService.findAllUsersTransfers(loggedUser);

		// THEN
		assertEquals(50, expected.get(1).getAmount());*/

		fail("not yet implemented");
	}

	@Test
	void isAccountBalanceSufficient_Yes_Test() throws InsufficientBalanceException {

		// GIVEN
		// WHEN
		// THEN
		assertTrue(transferService.isAccountBalanceSufficient(transferDto1, 105));

	}

	@Test
	void isAccountBalanceSufficient_No_Test() throws InsufficientBalanceException {

		// GIVEN
		// WHEN
		// THEN
		assertThrows(InsufficientBalanceException.class, ()-> transferService.isAccountBalanceSufficient(transferDto1, 10));

	}

}
