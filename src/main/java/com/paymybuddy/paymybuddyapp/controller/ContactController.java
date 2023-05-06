package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddyapp.exception.ContactNotFoundException;
import com.paymybuddy.paymybuddyapp.exception.LoggedUserException;
import com.paymybuddy.paymybuddyapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
public class ContactController {

	private UserService userService;

	private String message;

	public ContactController(UserService userService) {
		this.userService = userService;
	}

	public String getMessage() {
		return message;
	}

	@GetMapping("/user/contact")
	public String contact(Model model) {
		String friendEmail = "";
		model.addAttribute("email", friendEmail);
		List<User> contacts = userService.getLoggedUser().getContacts();
		model.addAttribute("contacts", contacts);
		model.addAttribute("message", message);
		log.info("Contact page");
		return "contact";
	}

	@PostMapping("/user/contact/save")
	public String addContact(@ModelAttribute("email") String email,
	                         BindingResult result,
	                         Model model) {

		try {
			userService.addContact(email);
			message = "Your friend was successfully added !";
			log.info("Contact success page");
			return "redirect:/user/contact?success";

		} catch (Exception exception) {
			if (exception instanceof LoggedUserException) {
				message = "You can't add yourself to your list of contacts";
			} else if (exception instanceof ContactNotFoundException) {
				message = "This contact was not found";
			} else if (exception instanceof ContactAlreadyExistsException) {
				message = "This contact is already in your list";
			} else {
				message = "Error, try again";
			}
			log.info("Contact error page");
			return "redirect:/user/contact?error";
		}
	}

}
