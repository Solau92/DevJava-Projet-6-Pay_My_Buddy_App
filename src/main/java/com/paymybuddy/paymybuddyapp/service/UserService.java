package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;

public interface UserService {

	/**
	 *
	 * @param userDto
	 */
	void saveUser(UserDto userDto);

	/**
	 *
	 * @param userDto
	 */
	void updateUser(UserDto userDto);

	/**
	 *
	 * @param email
	 * @return
	 */
	User findUserByEmail(String email);

	/**
	 *
	 * @param id
	 * @return
	 */
	String findUserEmailById(Integer id);

	/**
	 *
	 * @param friendEmail
	 */
	void addContact (String friendEmail) throws Exception;

	/**
	 *
	 * @param transferDto
	 * @throws Exception
	 */
	void addTransfer(TransferDto transferDto) throws Exception;

	/**
	 *
	 * @param amount
	 * @throws Exception
	 */
	void addMoney(double amount) throws Exception;

	/**
	 *
	 * @param amountWithdrawn
	 * @throws Exception
	 */
	void withdrawMoney(double amountWithdrawn) throws Exception;


	User getLoggedUser();
}
