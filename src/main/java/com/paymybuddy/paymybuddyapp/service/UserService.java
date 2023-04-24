package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(UserDto userDto);

    void updateUser(UserDto userDto);

    User findUserByEmail(String email);

	String findUserEmailById(Integer id);

	List<UserDto> findAllUsers();

    void addContact(User friend);

    void addTransfer(TransferDto transferDto);

	boolean isFriendAlreadyInList(User loggedUser, String email);

	void addMoney(double amount) throws Exception;

	void withdrawMoney();


}
