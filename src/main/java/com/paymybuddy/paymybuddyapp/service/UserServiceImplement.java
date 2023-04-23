package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.AmountZeroException;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplement implements UserService {

	private UserRepository userRepository;
	private TransferRepository transferRepository;

	private PasswordEncoder passwordEncoder;

	public UserServiceImplement(UserRepository userRepository, TransferRepository transferRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.transferRepository = transferRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void saveUser(UserDto userDto) {
		User user = new User();
		user.setFirstname(userDto.getFirstname());
		user.setLastname(userDto.getLastname());
		user.setEmail(userDto.getEmail());
		user.setAccountBalance(userDto.getAccountBalance());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userRepository.save(user);
	}

	@Override
	public void updateUser(UserDto userDto) {
		User user = findUserByEmail(userDto.getEmail());
		user.setFirstname(userDto.getFirstname());
		user.setLastname(userDto.getLastname());
		user.setEmail(userDto.getEmail());
		user.setAccountBalance(userDto.getAccountBalance());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userRepository.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map((user) -> mapToUserDto(user))
				.collect(Collectors.toList());
	}

	@Override
	public void addContact(User friend) {
		User user = getLoggedUser();
		user.getContacts().add(friend);
		userRepository.save(user);
	}

	@Override
	public void addTransfer(TransferDto transferDto) {

		User user = getLoggedUser();
		double accountModUser = user.getAccountBalance() - transferDto.getAmount()*1.05;
		user.setAccountBalance(accountModUser);

		User friend = userRepository.findByEmail(transferDto.getCreditorEmail());
		double accountModFriend = friend.getAccountBalance() + transferDto.getAmount();
		friend.setAccountBalance(accountModFriend);

		userRepository.save(user);
		userRepository.save(friend);

		//        Transfer transfer = new Transfer();
		//        transfer.setDate(transferDto.getDate());
		//        transfer.setReason(transferDto.getReason());
		//        transfer.setDebtor(transferDto.getDebtor());
		//        transfer.setCreditor(transferDto.getCreditor());
		//        user.getTransfers_done().add(transfer);

/*      User userdeb = getLoggedUser();
        Optional<User> usercred = userRepository.findById(transferDto.getCreditor());

        Transfer transfer = new Transfer();
        transfer.setDate(transferDto.getDate());
        transfer.setReason(transferDto.getReason());
        transfer.setDebtor(transferDto.getDebtor());
        transfer.setCreditor(transferDto.getCreditor());
        userdeb.getTransfers_done().add(transfer);

        userdeb.setAccountBalance(userdeb.getAccountBalance() - transferDto.getAmount());
        usercred.get().setAccountBalance(usercred.get().getAccountBalance() + transferDto.getAmount());
        userRepository.save(userdeb);
        userRepository.save(usercred.get());*/
	}

	@Override
	public boolean isFriendAlreadyInList(User loggedUser, String friendEmail) {

		List<User> contacts = loggedUser.getContacts();
		for (User u : contacts) {
			if (u.getEmail().equals(friendEmail)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addMoney(double amount) throws Exception {

		if(amount == 0) {
			throw new AmountZeroException();
		}
		User user = getLoggedUser();
		double accountModUser = user.getAccountBalance() + amount;
		user.setAccountBalance(accountModUser);
		userRepository.save(user);
	}

	@Override
	public void withdrawMoney() {
		User user = getLoggedUser();
		user.setAccountBalance(0);
		userRepository.save(user);
	}

	private UserDto mapToUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setFirstname(user.getFirstname());
		userDto.setLastname(user.getLastname());
		userDto.setEmail(user.getEmail());
		userDto.setAccountBalance(user.getAccountBalance());
		return userDto;
	}

	private User getLoggedUser() {
		User loggedUser = findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return loggedUser;
	}
}
