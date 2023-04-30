package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ContactServiceImpl implements ContactService {

	private UserService userService;

	public ContactServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean isContactValid(User user, User friend) throws LoggedUserException, ContactNotFoundException, ContactAlreadyExistsException, Exception {

		if(Objects.isNull(friend)) {
			throw new ContactNotFoundException();
		} else if(friend.getEmail().equals(user.getEmail())) {
			throw new LoggedUserException();
		} else if(isFriendAlreadyInList(user, friend)) {
			throw new ContactAlreadyExistsException();
		}
		return true;
	}

	public boolean isFriendAlreadyInList(User loggedUser, User friend) {

		List<User> contacts = loggedUser.getContacts();
		for (User u : contacts) {
			if (u.getEmail().equals(friend.getEmail())) {
				return true;
			}
		}
		return false;
	}

}
