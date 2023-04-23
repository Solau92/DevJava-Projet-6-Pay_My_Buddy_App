package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.AmountZeroException;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

	private UserService userService;

	private String message;



	public HomeController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user/home")
	public String home(Model model) {
		User user = getLoggedUser();
		model.addAttribute("firstname", user.getFirstname());
		model.addAttribute("accountBalance", (Double.toString(user.getAccountBalance()) + " €"));
		model.addAttribute("message", message);
		return "home";
	}

	@PostMapping ("user/home/addmoney")
		public String addMoney(@ModelAttribute("amount") double amount,
	                           BindingResult result,
	                           Model model) {

		try{
			userService.addMoney(amount);
			this.message = "Sous ajoutés";
			return "redirect:/user/home?successAdd";
		} catch (Exception exception) {

			if(exception instanceof AmountZeroException) {
				this.message = "Amount = 0";
			} else {
				this.message = "Erreur";

			}
			return "redirect:/user/home?errorAdd";
		}

	}

	@PostMapping ("user/home/withdrawmoney")
	public String withdrawMoney(Model model) {

		userService.withdrawMoney();
		this.message = "Sous envoyés";
		return "redirect:/user/home?successWithdraw";
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
