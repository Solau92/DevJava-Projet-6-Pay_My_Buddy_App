package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.service.UserService;
import jakarta.validation.Valid;
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
public class AuthenticationController {

	private UserService userService;

	public AuthenticationController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/index")
	public String index() {
		log.info("Index page");
		return "index";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		log.info("Register page");
		return "register";
	}

	@PostMapping("/register/save")
	public String registration(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Model model) {

		if (userService.findUserByEmail(userDto.getEmail()) != null) {
			result.rejectValue("email", null, "There is already an account registered with this email");
			log.info("Register page with error");
			return "register";
		}

		if(result.hasErrors()){
			log.info("Register page with error");
			return "register";
		}

		userService.saveUser(userDto);
		log.info("Index success page");

		return "redirect:/index?success";
	}

	@GetMapping("/login")
	public String login() {
		log.info("Login page");
		return "login";
	}

	@GetMapping("/logoff")
	public String logoff() {
		log.info("Logoff page");
		return "logoff";
	}

}
