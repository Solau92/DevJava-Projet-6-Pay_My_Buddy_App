package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
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

	public UserService userService;

	public ContactController(UserService userService){
		this.userService = userService;
	}

	@GetMapping("/user/contact")
	public String contact(Model model) {
		String friendEmail = "";
		model.addAttribute("email", friendEmail);
		List<User> contacts = getLoggedUser().getContacts();
		model.addAttribute("contacts", contacts);
		return "contact";
	}

	@PostMapping("/user/contact/save")
	public String addContact(@ModelAttribute("email") String email,
	                         BindingResult result,
	                         Model model) {

		System.out.println("friend email : " + email);
		User friend = userService.findUserByEmail(email);

		if(!Objects.isNull(friend)) {
			System.out.println("friend non null, id : " + friend.getId());
			userService.addContact(friend);
		}

/*		if (Objects.isNull(friend)){
			result.rejectValue("email", null, "This email was not found");
			System.out.print("Friend is null");
		}*/

/*		if (result.hasErrors()){
			System.out.print("Result has error");
			return "contact";
		}*/

		return "contact";
	}

	private User getLoggedUser() {
		User loggedUser = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return loggedUser;
	}


}
