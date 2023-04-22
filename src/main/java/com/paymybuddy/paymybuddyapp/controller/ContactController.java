package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;
import com.paymybuddy.paymybuddyapp.service.ContactService;
import com.paymybuddy.paymybuddyapp.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class ContactController {

	private UserService userService;

	private ContactService contactService;

	private String errorMessage;

	public ContactController(UserService userService, ContactService contactService){
		this.userService = userService;
		this.contactService = contactService;
	}

	@GetMapping("/user/contact")
	public String contact(Model model) {
		String friendEmail = "";
		model.addAttribute("email", friendEmail);
		List<User> contacts = getLoggedUser().getContacts();
		model.addAttribute("contacts", contacts);
		model.addAttribute("message", errorMessage);
		return "contact";
	}

	@PostMapping("/user/contact/save")
	public String addContact(@ModelAttribute("email") String email,
	                         BindingResult result,
	                         Model model) {

		try {
			contactService.isContactValid(email);
			User friend = userService.findUserByEmail(email);
			userService.addContact(friend);
			return "redirect:/user/contact?success";
		} catch (Exception exception) {
			if(exception instanceof LoggedUserException) {
				errorMessage = "You can't add yourself to your list of contacts";
			} else if (exception instanceof ContactNotFoundException){
				errorMessage = "This contact was not found";
			} else if (exception instanceof ContactAlreadyExistsException) {
				errorMessage = "This contact is already in your list";
			} else {
				errorMessage = "Error, try again";
			}
			return "redirect:/user/contact?error";
		}
	}

	private User getLoggedUser() {
		User loggedUser = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return loggedUser;
	}


/*	@PostMapping("/user/contact/save")
	public String addContact(@ModelAttribute("email") String email,
	                         BindingResult result,
	                         Model model) {

		User friend = userService.findUserByEmail(email);

		// Si friend not null = contact trouvé et que différent du loggeduser
		if(!Objects.isNull(friend) && !getLoggedUser().getEmail().equals(email)) {
			userService.addContact(friend);
		}

*//*		if (Objects.isNull(friend)){
			result.rejectValue("email", null, "This email was not found");
			System.out.print("Friend is null");
		}

		if (result.hasErrors()){
			System.out.print("Result has error");
			return "contact";
		}*//*

		return "redirect:/user/contact";
	}*/

	/*	@PostMapping("/user/contact/save")
	public String addContact(@ModelAttribute("user") @Valid UserDto userDto,
	                         BindingResult result,
	                         Model model) {

		User friend = userService.findUserByEmail(userDto.getEmail());

		// Si friend not null = contact trouvé et que différent du loggeduser
		if(!Objects.isNull(friend) && !getLoggedUser().getEmail().equals(userDto.getEmail())) {
			userService.addContact(friend);
		}

		if (Objects.isNull(friend)){
			result.rejectValue("email", null, "This email was not found");
			System.out.print("Friend is null");
		}

		if (result.hasErrors()){
			System.out.print("Result has error");
			return "contact";
		}

		return "redirect:/user/contact";
	}*/


}
