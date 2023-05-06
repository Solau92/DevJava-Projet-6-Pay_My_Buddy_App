package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.IncorrectAmountException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class HomeController {

	private UserService userService;

	private String message;

	public HomeController(UserService userService) {
		this.userService = userService;
	}

	public String getMessage() {
		return message;
	}

	@GetMapping("/user/home")
	public String home(Model model) {

		User loggedUser = userService.getLoggedUser();

		if (loggedUser == null) {
			message = "Logged user not found";
			log.info("Home error page");
			return "redirect:/user/home";
		}

		model.addAttribute("firstname", loggedUser.getFirstname());
		model.addAttribute("accountBalance", (Double.toString(loggedUser.getAccountBalance()) + " €"));
		model.addAttribute("message", message);

		log.info("Home page");

		return "home";
	}

	@PostMapping("user/home/addmoney")
	public String addMoney(@ModelAttribute("amount") double amount,
	                       BindingResult result,
	                       Model model) {

		try {
			userService.addMoney(amount);
			this.message = "Money was successfully added to your account";
			log.info("Home add money success page");
			return "redirect:/user/home?successAdd";

		} catch (Exception exception) {
			if (exception instanceof IncorrectAmountException) {
				this.message = "Error, the amount cannot be equal to 0 €";
			} else {
				this.message = "Error";
			}
			log.info("Home add money error page");
			return "redirect:/user/home?errorAdd";
		}
	}

	@PostMapping("user/home/withdrawmoney")
	public String withdrawMoney(@ModelAttribute("amountWithdrawn") double amountWithdrawn, Model model) {

		try {
			userService.withdrawMoney(amountWithdrawn);
			message = "Money was sent to your bank account";
			log.info("Home success withdraw page");
			return "redirect:/user/home?successWithdraw";

		} catch (Exception exception) {
			if (exception instanceof InsufficientBalanceException) {
				message = "You can't withdraw more than the amount of your account balance";
			} else {
				message = "Error";
			}
			log.info("Home error withdraw page");
			return "redirect:/user/home?errorWithdraw";
		}
	}

}
