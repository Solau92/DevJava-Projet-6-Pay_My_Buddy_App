package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ProfileController {

	private UserService userService;

	public ProfileController(UserService userService){
		this.userService = userService;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	@GetMapping("/user/profile")
	public String profile(Model model) {

		User user = getLoggedUser();

		if (user == null) {
			message = "Logged user not found";
			return "redirect:/user/profile?error";
		}

		model.addAttribute("firstname", user.getFirstname());
		model.addAttribute("lastname", user.getLastname());
		model.addAttribute("email", user.getEmail());
		return "profile";
	}

	@GetMapping("/user/profileEdition")
	public String profileEdition(Model model) {

		// Permet de transmettre les infos à la vue
		User user = getLoggedUser();
		model.addAttribute("firstname", user.getFirstname());
		model.addAttribute("lastname", user.getLastname());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("password", user.getPassword());

		// Permet de récupérer des infos de la vue
		User updatedUser = getLoggedUser();
		model.addAttribute("userupdated", updatedUser);

		return "profileEdition";
	}

	@PutMapping("/user/update/save")
	public String update(@ModelAttribute("userupdated") @Valid UserDto userDto,
	                     BindingResult result,
	                     Model model) {

		User existingUser = userService.findUserByEmail(userDto.getEmail());

		userService.updateUser(userDto);
		return "redirect:/profile";
	}

	private User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userService.findUserByEmail(authentication == null ? "" : authentication.getName());
	}

}
