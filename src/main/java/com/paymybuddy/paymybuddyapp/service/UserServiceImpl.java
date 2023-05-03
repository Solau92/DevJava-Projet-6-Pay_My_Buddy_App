package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.*;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private ContactService contactService;

	private PasswordEncoder passwordEncoder;

//	@Value("${project.fees}")
	private static double FEES = 0.5/100;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy ContactService contactService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.contactService = contactService;
	}

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

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public String findUserEmailById(Integer id) {
		return userRepository.findById(id).get().getEmail();
	}

	@Override
	public void addContact(String friendEmail) throws Exception {

		User user = getLoggedUser();
		User friend = findUserByEmail(friendEmail);

		contactService.isContactValid(user, friend);

		user.getContacts().add(friend);

		userRepository.save(user);
	}

	@Override
	public void addTransfer(TransferDto transferDto) {

		User user = getLoggedUser();
		double accountModUser = user.getAccountBalance() - transferDto.getAmount() - transferDto.getAmount()*FEES;
		accountModUser = Math.ceil(accountModUser*100)/100;
		user.setAccountBalance(accountModUser);

		User friend = userRepository.findByEmail(transferDto.getCreditorEmail());
		double accountModFriend = friend.getAccountBalance() + transferDto.getAmount();
		friend.setAccountBalance(accountModFriend);

		userRepository.save(user);
		userRepository.save(friend);

	}

	@Override
	public void addMoney(double amount) throws Exception {

		if(amount == 0) {
			throw new IncorrectAmountException("Amount equals zero");
		}
		User user = getLoggedUser();
		double accountModUser = user.getAccountBalance() + amount;
		user.setAccountBalance(accountModUser);
		userRepository.save(user);
	}

	@Override
	public void withdrawMoney(double amountWithdrawn) throws Exception {

		User user = getLoggedUser();

		if(amountWithdrawn > user.getAccountBalance()) {
			log.error("InsufficientBalanceException");
			throw new InsufficientBalanceException();
		}

		double accountModUser = user.getAccountBalance() - amountWithdrawn;
		user.setAccountBalance(accountModUser);
		userRepository.save(user);
	}

	@Override
	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.findUserByEmail(authentication == null ? "" : authentication.getName());
	}

}
