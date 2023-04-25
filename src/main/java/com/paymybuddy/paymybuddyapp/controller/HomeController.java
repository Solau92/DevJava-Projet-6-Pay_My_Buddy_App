package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.AmountZeroException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

@Controller
public class HomeController {

	private UserService userService;

	public String getMessage() {
		return message;
	}

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
			this.message = "Money was successfully added to your account";
			return "redirect:/user/home?successAdd";

		} catch (Exception exception) {
			if(exception instanceof AmountZeroException) {
				this.message = "Error, the amount cannot be equal to 0 €";
			} else {
				this.message = "Error";
			}
			return "redirect:/user/home?errorAdd";
		}

	}

	@PostMapping ("user/home/withdrawmoney")
	public String withdrawMoney(@ModelAttribute("amountWithdrawn") double amountWithdrawn, Model model) {

		try {
			userService.withdrawMoney(amountWithdrawn);
			message = "Money was sent to your bank account";
			return "redirect:/user/home?successWithdraw";

		} catch (Exception exception){
			if (exception instanceof InsufficientBalanceException) {
				message = "You can't withdraw more than the amount of your account balance";
			} else {
				message = "Error";
			}
			return "redirect:/user/home?errorWithdraw";
		}
	}

	private User getLoggedUser() {
		User loggedUser = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return loggedUser;
	}

}
