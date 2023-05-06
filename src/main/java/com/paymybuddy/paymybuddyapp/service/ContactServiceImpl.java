package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

	private UserService userService;

	public ContactServiceImpl(UserService userService) {
		this.userService = userService;
	}

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
	@Override
	public boolean isContactValid(User user, User friend) throws LoggedUserException, ContactNotFoundException, ContactAlreadyExistsException, Exception {

		if (Objects.isNull(friend)) {
			log.error("ContactNotFoundException");
			throw new ContactNotFoundException();
		} else if (friend.getEmail().equals(user.getEmail())) {
			log.error("LoggedUserException");
			throw new LoggedUserException();
		} else if (isFriendAlreadyInList(user, friend)) {
			log.error("ContactAlreadyExistsException");
			throw new ContactAlreadyExistsException();
		}
		return true;
	}

	/**
	 * Returns true if the contact is already in loggedUser'"'s list of contacts, false otherwise.
	 *
	 * @param loggedUser
	 * @param friend
	 * @return true if the contact is already in loggedUser's list of contacts, false otherwise
	 */
	public boolean isFriendAlreadyInList(User loggedUser, User friend) {

		List<User> contacts = loggedUser.getContacts();
		for (User u : contacts) {
			if (u.getEmail().equals(friend.getEmail())) {
				return true;
			}
		}
		log.error("FriendAlreadyInList");
		return false;
	}

}
