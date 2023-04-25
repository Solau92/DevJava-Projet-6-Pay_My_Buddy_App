package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.TransferService;
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

	private String LoggedUserEmail;

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
	public String registration(@ModelAttribute("user") @Valid UserDto userDto,
	                           BindingResult result,
	                           Model model) {

		if(userService.findUserByEmail(userDto.getEmail()) != null) {
			result.rejectValue("email", null,
					"There is already an account registered with this email");
		}

		if (result.hasErrors()) {
			return "register";
		}

		userService.saveUser(userDto);
		return "redirect:/index?success";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

/*	@GetMapping("/user/home")
	public String home(Model model) {
		getEmail();
		User user = userService.findUserByEmail(this.LoggedUserEmail);
		model.addAttribute("accountBalance", (Double.toString(user.getAccountBalance()) + " €"));
		return "home";
	}*/

	@GetMapping("/logoff")
	public String logoff() {
		return "logoff";
	}

/*	@GetMapping("/users")
	public String users(Model model) {
		List<UserDto> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "users";
	}*/

/*	private void getEmail() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		this.LoggedUserEmail = username;
	}*/


/*	@GetMapping("/user/profile")
	public String profile(Model model) {
		getEmail();
		User user = userService.findUserByEmail(this.LoggedUserEmail);
		model.addAttribute("firstname", user.getFirstname());
		model.addAttribute("lastname", user.getLastname());
		model.addAttribute("email", user.getEmail());
		return "profile";
	}

	@GetMapping("/user/profileEdition")
	public String profileEdition(Model model) {
		User user = userService.findUserByEmail(this.LoggedUserEmail);
		model.addAttribute("firstname", user.getFirstname());
		model.addAttribute("lastname", user.getLastname());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("password", user.getPassword());
		User userupdated = userService.findUserByEmail(this.LoggedUserEmail);
		model.addAttribute("userupdated", userupdated);
		return "profileEdition";
	}*/

/*	@PostMapping("/update/save")
	public String update(@ModelAttribute("userupdated") UserDto userDto,
	                     BindingResult result,
	                     Model model) {

		//est-ce que l'utilisateru connecté est bien le user à mettre à jour
		User existingUser = userService.findUserByEmail(userDto.getEmail());

*//*		if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null,
					"There is already an account registered with the same email");
		}

		if (userDto.getEmail().equals("")) {
			result.rejectValue("email", null, "Email field cannot be empty");
		}

		if (result.hasErrors()) {
			model.addAttribute("userupdated", userDto);
			return "register";
		}*//*

		userService.saveUser(userDto);
		return "redirect:/profile";
	}*/

/*
*
	private void getEmail() {
		User usserlogged = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}

		this.LoggedUserEmail = username;
	}

	private void getEmail() {
//		User userLogged = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}

		this.LoggedUserEmail = username;
	}*/


/*	@GetMapping("/user/transfer")
	public String transfer(Model model) {
		TransferDto transfer = new TransferDto();
		model.addAttribute("transfer", transfer);
		return "transfer";
	}

	@PostMapping("/user/transfer/pay")
	public String addTransfer(@ModelAttribute("transfer") TransferDto transferDto,
	                           BindingResult result,
	                           Model model) {
		Integer idLoggedUser = userService.findUserByEmail(this.LoggedUserEmail).getId();
//		Integer idLoggedUser = this.LoggedUserEmail;
		transferDto.setDebtor(idLoggedUser);
		transferService.saveTransfer(transferDto);
		return "redirect:/transfer";
	}*/


/*	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "register";
	}*/

/*	@GetMapping("/user/contact")
	public String contact(Model model) {
		String friendEmail = "";
		model.addAttribute("friendemail", friendEmail);
		return "contact";
	}

	@PostMapping("/user/contact")
	public String addContact(@ModelAttribute("user") UserDto userDto,
	                           BindingResult result,
	                           Model model) {
//TODO !!
	*//*	User existingUser = userService.findUserByEmail(userDto.getEmail());

		if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null,
					"There is already an account registered with the same email");
		}

		if (userDto.getEmail().equals("")) {
			result.rejectValue("email", null, "Email field cannot be empty");
		}

		if (result.hasErrors()) {
			model.addAttribute("user", userDto);
			return "contact";
		}*//*

		userService.saveUser(userDto);
		return "contact";
	}*/



}
