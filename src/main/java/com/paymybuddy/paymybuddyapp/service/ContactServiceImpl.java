package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ContactServiceImpl implements ContactService {

	private UserService userService;

	public ContactServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean isContactValid(String friendEmail) throws Exception {

		if(friendEmail.equals(getLoggedUser().getEmail())) {
			throw new LoggedUserException();
		} else if(Objects.isNull(userService.findUserByEmail(friendEmail))) {
			throw new ContactNotFoundException();
		} else if(userService.isFriendAlreadyInList(friendEmail)) {
			throw new ContactAlreadyExistsException();
		}
		return true;
	}

	private User getLoggedUser() {
		User loggedUser = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return loggedUser;
	}

}
