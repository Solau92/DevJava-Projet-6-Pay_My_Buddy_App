package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ProfileController {

	private UserService userService;
	private String message;

	public ProfileController(UserService userService) {
		this.userService = userService;
	}

	public String getMessage() {
		return message;
	}

	@GetMapping("/user/profile")
	public String profile(Model model) {

		User user = userService.getLoggedUser();

		if (user == null) {
			message = "Logged user not found";
			log.info("Profile error page");
			return "redirect:/user/profile?error";
		}

		model.addAttribute("firstname", user.getFirstname());
		model.addAttribute("lastname", user.getLastname());
		model.addAttribute("email", user.getEmail());

		log.info("Profile page");

		return "profile";
	}

}
