package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private UserService userService;

	public HomeController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user/home")
	public String home(Model model) {
		User user = getLoggedUser();
		model.addAttribute("accountBalance", (Double.toString(user.getAccountBalance()) + " €"));
		return "home";
	}

	private User getLoggedUser() {
		User loggedUser = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return loggedUser;
	}

	/*private void getEmail() {
		User usserlogged = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}

		this.LoggedUserEmail = username;
	}*/

}
