package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.TransferService;
import com.paymybuddy.paymybuddyapp.service.TransferServiceImpl;
import com.paymybuddy.paymybuddyapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthenticationController {

	private UserService userService;

	private TransferService transferService;

	public AuthenticationController(UserService userService, TransferService transferService) {
		this.userService = userService;
		this.transferService = transferService;
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "register";
	}

	@PostMapping("/register/save")
	public String registration(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Model model) {

		if (userService.findUserByEmail(userDto.getEmail()) != null) {
			result.rejectValue("email", null, "There is already an account registered with this email");
			return "register";
		}

		userService.saveUser(userDto);
		return "redirect:/index?success";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/logoff")
	public String logoff() {
		return "logoff";
	}

}
