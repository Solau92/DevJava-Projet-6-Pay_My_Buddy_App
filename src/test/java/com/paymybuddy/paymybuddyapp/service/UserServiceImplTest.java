package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private TransferRepository transferRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	private User loggedUser = new User();
	private UserDto userDto = new UserDto();

	private User friend = new User();

	private static Transfer transfer = new Transfer();

	private static TransferDto transferDto1 = new TransferDto();

	private String encodedPassword;

	@BeforeEach
	void setUp() {
		encodedPassword = "encodedPassword";

		loggedUser.setId(1);
		loggedUser.setFirstname("firstnameTest");
		loggedUser.setLastname("lastnameTest");
		loggedUser.setEmail("emailTest@email.com");
		loggedUser.setPassword(encodedPassword);
		loggedUser.setAccountBalance(105.00);

		userDto.setId(1);
		userDto.setFirstname("firstnameTest");
		userDto.setLastname("lastnameTest");
		userDto.setEmail("emailTest@email.com");
		userDto.setPassword("passwordTest");
		userDto.setAccountBalance(105.00);

		friend.setId(2);
		friend.setFirstname("firstnameFriendTest");
		friend.setLastname("lastnameFriendTest");
		friend.setEmail("emailFriendTest@email.com");
		friend.setPassword(encodedPassword);
		friend.setAccountBalance(210.00);

		transfer.setId(1);
		transfer.setDebtor(2);
		transfer.setCreditor(1);
		transfer.setReason("because");
		transfer.setAmount(100);
		transfer.setDate(LocalDate.now());

		transferDto1.setDebtor(2);
		transferDto1.setCreditor(1);
		transferDto1.setReason("because");
		transferDto1.setAmount(100);
		transferDto1.setDate(LocalDate.now());
		transferDto1.setCreditorEmail("friendEmailTest@email.com");
	}

	@Test
	void saveUser_Ok_Test(){

		// GIVEN
		when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		userService.saveUser(userDto);

		// THEN
		// Problème pour tester car ma méthode saveUser ne renvoie rien...
		fail("not yet implemented");
	}


	@Test
	void updateUser_Ok_Test(){

		// GIVEN

		// WHEN

		// THEN

		fail("not yet implemented");
	}

	@Test
	void findUserByEmail_Ok_Test(){

		// GIVEN
		when(userRepository.findByEmail(anyString())).thenReturn(loggedUser);

		// WHEN
		User userFound = userService.findUserByEmail("email@email.com");

		// THEN
		assertEquals(1, userFound.getId());
		assertEquals(105, userFound.getAccountBalance());
		assertEquals("encodedPassword", loggedUser.getPassword());

	}

	@Test
	void findUserEmailById_Ok_Test(){

		// GIVEN
		Optional<User> optionalUser = Optional.of(loggedUser);
		when(userRepository.findById(anyInt())).thenReturn(optionalUser);

		// WHEN
		String expectedEmail = userService.findUserEmailById(1);

		// THEN
		assertEquals("emailTest@email.com", loggedUser.getEmail());

	}

	@Test
	void addContact_Ok_Test(){

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(userRepository.save(any(User.class))).thenReturn(friend);

		// WHEN
		userService.addContact(friend);

		// THEN
		assertEquals(friend, loggedUser.getContacts().get(0));
	}

	@Test
	void addTransfer_Ok_Test(){

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser).thenReturn(friend);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser).thenReturn(friend);

		// WHEN
		userService.addTransfer(transferDto1);

		// THEN
		assertEquals(0.0, loggedUser.getAccountBalance());
		assertEquals(310.0, friend.getAccountBalance());
	}

	@Test
	void isFriendAlreadyInList_Yes_Test(){




		fail("not yet implemented");
	}

	@Test
	void isFriendAlreadyInList_No_Test(){

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);

		// WHEN
		Boolean result = userService.isFriendAlreadyInList(friend.getEmail());

		// THEN
		assertFalse(result);

	}

	@Test
	void addMoney_Ok_Test(){

		// GIVEN

		// WHEN

		// THEN

		fail("not yet implemented");
	}

	@Test
	void withdrawMoney_Ok_Test(){

		// GIVEN

		// WHEN

		// THEN

		fail("not yet implemented");
	}


}
