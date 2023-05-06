package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;

public interface ContactService {

	/**
	 * Returns true if the contact is valid, throws an exception otherwise.
	 *
	 * @param user
	 * @param friend
	 * @return true if the contact is valid.
	 * @throws LoggedUserException
	 * @throws ContactNotFoundException
	 * @throws ContactAlreadyExistsException
	 * @throws Exception
	 */
	public boolean isContactValid(User user, User friend) throws LoggedUserException, ContactNotFoundException, ContactAlreadyExistsException, Exception;
}
