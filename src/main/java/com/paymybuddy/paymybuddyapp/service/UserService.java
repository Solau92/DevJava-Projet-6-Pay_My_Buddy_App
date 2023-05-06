package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.UserNotFoundException;

public interface UserService {

	/**
	 * Saves a User.
	 *
	 * @param userDto
	 * @return the User saved
	 */
	User saveUser(UserDto userDto);

	/**
	 * Updates user's details.
	 *
	 * @param userDto
	 * @return the User updated
	 */
	User updateUser(UserDto userDto);

	/**
	 * Searches a User given an email.
	 *
	 * @param email
	 * @return the User found
	 */
	User findUserByEmail(String email);

	/**
	 * Searches a User's email given his id.
	 *
	 * @param id
	 * @return the email, if the User was found
	 * @throws UserNotFoundException if the User was not found
	 */
	String findUserEmailById(Integer id) throws UserNotFoundException;

	/**
	 * Adds and saves contact, given the friend's email.
	 *
	 * @param friendEmail
	 * @throws Exception if the contact is not valid
	 */
	void addContact(String friendEmail) throws Exception;

	/**
	 * Adds and saves transfer in database, if the transfer is valid.
	 *
	 * @param transferDto
	 * @throws Exception
	 */
	void addTransfer(TransferDto transferDto) throws Exception;

	/**
	 * Adds and saves in data base the given amount to the User account from his bank account.
	 *
	 * @param amount
	 * @throws Exception if the amount is not valid
	 */
	void addMoney(double amount) throws Exception;

	/**
	 * Send the amount given from User's account to his bank account.
	 *
	 * @param amountWithdrawn
	 * @throws Exception if the amount is not valid
	 */
	void withdrawMoney(double amountWithdrawn) throws Exception;

	/**
	 * Returns the current logged User.
	 *
	 * @return User, null if not found
	 */
	User getLoggedUser();
}
