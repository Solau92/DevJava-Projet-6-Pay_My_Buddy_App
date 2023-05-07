package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.*;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private ContactService contactService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private SecurityContext securityContext;

	@Captor
	ArgumentCaptor<User> userCaptor;

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
		loggedUser.setAccountBalance(100.5);

		userDto.setId(1);
		userDto.setFirstname("firstnameTest");
		userDto.setLastname("lastnameTest");
		userDto.setEmail("emailTest@email.com");
		userDto.setPassword("passwordTest");
		userDto.setAccountBalance(100.5);

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
		User userSaved = userService.saveUser(userDto);

		// THEN
		verify(userRepository, Mockito.times(1)).save(any(User.class));
		assertEquals(loggedUser.getFirstname(), userSaved.getFirstname());
		assertEquals(loggedUser.getAccountBalance(), userSaved.getAccountBalance());
	}


	@Test
	void updateUser_Ok_Test(){

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		User userUpdated = userService.updateUser(userDto);

		// THEN
		verify(userRepository, Mockito.times(1)).save(any(User.class));
		assertEquals(loggedUser.getFirstname(), userUpdated.getFirstname());
		assertEquals(loggedUser.getAccountBalance(), userUpdated.getAccountBalance());
	}

	@Test
	void findUserByEmail_Ok_Test(){

		// GIVEN
		when(userRepository.findByEmail(anyString())).thenReturn(loggedUser);

		// WHEN
		User userFound = userService.findUserByEmail("email@email.com");

		// THEN
		assertEquals(1, userFound.getId());
		assertEquals(100.5, userFound.getAccountBalance());
		assertEquals("encodedPassword", loggedUser.getPassword());

	}

	@Test
	void findUserEmailById_Ok_Test() throws UserNotFoundException {

		// GIVEN
		Optional<User> optionalUser = Optional.of(loggedUser);
		when(userRepository.findById(anyInt())).thenReturn(optionalUser);

		// WHEN
		String expectedEmail = userService.findUserEmailById(1);

		// THEN
		assertEquals("emailTest@email.com", loggedUser.getEmail());

	}

	@Test
	void findUserEmailById_NotFound_Test() throws UserNotFoundException {

		// GIVEN
		Optional<User> optionalUser = Optional.empty();
		when(userRepository.findById(anyInt())).thenReturn(optionalUser);

		// WHEN
		// THEN
		assertThrows(UserNotFoundException.class, ()->userService.findUserEmailById(3));

	}

	@Test
	void addContact_Ok_Test() throws Exception {

		// GIVEN
		when(userService.getLoggedUser()).thenReturn(loggedUser);
		when(userService.findUserByEmail(anyString())).thenReturn(friend);
		when(contactService.isContactValid(any(User.class), any(User.class))).thenReturn(true);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		userService.addContact(friend.getEmail());

		// THEN
		verify(userRepository, Mockito.times(1)).save(userCaptor.capture());
		User friendAdded = userCaptor.getValue().getContacts().get(0);
	}

	@Test
	void addTransfer_Ok_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser).thenReturn(friend);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser).thenReturn(friend);

		// WHEN

		System.out.print(loggedUser.getAccountBalance());
		System.out.print(transferDto1.getAmount());

		userService.addTransfer(transferDto1);
		System.out.print(loggedUser.getAccountBalance());

		// THEN
		assertEquals(0.0, loggedUser.getAccountBalance());
		assertEquals(310.0, friend.getAccountBalance());
	}

	@Test
	void addTransfer_ContactNotFound_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser).thenReturn(null);

		// WHEN
		// THEN
		assertThrows(ContactNotFoundException.class, ()->userService.addTransfer(transferDto1));
		assertEquals(100.5, loggedUser.getAccountBalance());
		assertEquals(210.0, friend.getAccountBalance());
	}


	@Test
	void addMoney_Ok_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		userService.addMoney(80);

		// THEN
		assertEquals(180.5, loggedUser.getAccountBalance());
	}

	@Test
	void addMoney_AmountEqualsZero_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		// THEN
		assertThrows(IncorrectAmountException.class, ()->userService.addMoney(0));

	}

	@Test
	void withdrawMoney_Ok_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
		userService.withdrawMoney(100.5);

		// THEN
		assertEquals(0, loggedUser.getAccountBalance());
	}

	@Test
	void withdrawMoney_InsufficientBalance_Test() throws Exception {

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);
		when(userRepository.save(any(User.class))).thenReturn(loggedUser);

		// WHEN
				// THEN
		assertThrows(InsufficientBalanceException.class, ()->userService.withdrawMoney(200));
	}

	@Test
	void getLoggedUser_Ok_Test(){

		// GIVEN
		when(userService.findUserByEmail(anyString())).thenReturn(loggedUser);

		// WHEN
		User user = userService.getLoggedUser();

		// THEN
		assertNotNull(user);
	}

	@Test
	void getLoggedUser_UserNull_Test(){

		// GIVEN
		when(securityContext.getAuthentication()).thenReturn(null);

		// WHEN
		User user = userService.getLoggedUser();

		// THEN
		assertNull(user);

	}

}
