package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;

public interface ContactService {


	/**
	 *
	 * @param user
	 * @param friend
	 * @return
	 * @throws LoggedUserException
	 * @throws ContactNotFoundException
	 * @throws ContactAlreadyExistsException
	 */
	public boolean isContactValid(User user, User friend) throws LoggedUserException, ContactNotFoundException, ContactAlreadyExistsException, Exception;
}
