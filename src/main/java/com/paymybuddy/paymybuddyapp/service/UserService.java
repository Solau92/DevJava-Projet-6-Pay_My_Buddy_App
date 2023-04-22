package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    void updateUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    void addContact(User friend);

    void addTransfer(TransferDto transferDto);

	boolean isFriendAlreadyInList(User loggedUser, String email);
}
