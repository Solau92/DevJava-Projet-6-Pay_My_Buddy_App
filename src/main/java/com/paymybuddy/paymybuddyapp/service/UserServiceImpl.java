package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.*;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private static double FEES = 0.5 / 100;
	private UserRepository userRepository;
	private ContactService contactService;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy ContactService contactService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.contactService = contactService;
	}

	/**
	 * Saves a User in database.
	 *
	 * @param userDto
	 * @return the User saved
	 */
	@Override
	public User saveUser(UserDto userDto) {
		User user = new User();
		user.setFirstname(userDto.getFirstname());
		user.setLastname(userDto.getLastname());
		user.setEmail(userDto.getEmail());
		user.setAccountBalance(userDto.getAccountBalance());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return userRepository.save(user);
	}

	/**
	 * Updates user's details in database.
	 *
	 * @param userDto
	 * @return the User updated
	 */
	@Override
	public User updateUser(UserDto userDto) {
		User user = findUserByEmail(userDto.getEmail());
		user.setFirstname(userDto.getFirstname());
		user.setLastname(userDto.getLastname());
		user.setEmail(userDto.getEmail());
		user.setAccountBalance(userDto.getAccountBalance());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return userRepository.save(user);
	}

	/**
	 * Searches in database a User given an email.
	 *
	 * @param email
	 * @return the User found
	 */
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Searches in database a User's email given his id.
	 *
	 * @param id
	 * @return the email, if the User was found
	 * @throws UserNotFoundException if the User was not found in database
	 */
	@Override
	public String findUserEmailById(Integer id) throws UserNotFoundException {

		if (userRepository.findById(id).isEmpty()) {
			throw new UserNotFoundException();
		}
		return userRepository.findById(id).get().getEmail();
	}

	/**
	 * Adds and saves contact in database, given the friend's email.
	 *
	 * @param friendEmail
	 * @throws Exception if the contact is not valid
	 */
	@Override
	public void addContact(String friendEmail) throws Exception {

		User user = getLoggedUser();
		User friend = findUserByEmail(friendEmail);

		contactService.isContactValid(user, friend);

		user.getContacts().add(friend);

		userRepository.save(user);
	}

	/**
	 * Adds and saves transfer in database, if the transfer is valid.
	 *
	 * @param transferDto
	 * @throws Exception if the transfer cannot be done
	 */
	@Override
	public void addTransfer(TransferDto transferDto) throws Exception {

		User user = getLoggedUser();
		double accountModUser = Math.ceil(user.getAccountBalance() - transferDto.getAmount() - transferDto.getAmount() * FEES);
		accountModUser = Math.ceil(accountModUser * 100) / 100;
		user.setAccountBalance(accountModUser);

		User friend = userRepository.findByEmail(transferDto.getCreditorEmail());
		double accountModFriend = friend.getAccountBalance() + transferDto.getAmount();
		friend.setAccountBalance(accountModFriend);

		userRepository.save(user);
		userRepository.save(friend);

	}

	/**
	 * Adds and saves in data base the given amount to the User account from his bank account.
	 *
	 * @param amount
	 * @throws Exception if the amount is not valid
	 */
	@Override
	public void addMoney(double amount) throws Exception {

		if (amount == 0) {
			throw new IncorrectAmountException("Amount equals zero");
		}
		User user = getLoggedUser();
		double accountModUser = user.getAccountBalance() + amount;
		user.setAccountBalance(accountModUser);
		userRepository.save(user);
	}

	/**
	 * Send the amount given from User's account to his bank account, and saves in data base.
	 *
	 * @param amountWithdrawn
	 * @throws Exception if the amount is not valid
	 */
	@Override
	public void withdrawMoney(double amountWithdrawn) throws Exception {

		User user = getLoggedUser();

		if (amountWithdrawn > user.getAccountBalance()) {
			log.error("InsufficientBalanceException");
			throw new InsufficientBalanceException();
		}

		double accountModUser = user.getAccountBalance() - amountWithdrawn;
		user.setAccountBalance(accountModUser);
		userRepository.save(user);
	}

	/**
	 * Returns the current logged User.
	 *
	 * @return User, null if not found
	 */
	@Override
	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.findUserByEmail(authentication == null ? "" : authentication.getName());
	}

}
