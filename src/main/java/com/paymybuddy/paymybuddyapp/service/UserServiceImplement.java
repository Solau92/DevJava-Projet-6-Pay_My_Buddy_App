package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
//import com.paymybuddy.paymybuddyapp.repository.ContactRepository;
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

/*
    private ContactRepository contactRepository;
*/

    private PasswordEncoder passwordEncoder;

    public UserServiceImplement(UserRepository userRepository, TransferRepository transferRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
        this.passwordEncoder = passwordEncoder;
//        this.contactRepository = contactRepository;
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
        System.out.println("Dans add contact, user id : " + user.getId());
        System.out.println("Dans add contact, friend id : " + friend.getId());
            user.printContact();
        user.getContacts().add(friend);
            user.printContact();
        userRepository.save(user);
//        contactRepository.save();
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
