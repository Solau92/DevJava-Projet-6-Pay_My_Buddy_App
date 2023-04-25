package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.service.TransferService;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

@Controller
public class TransferController {

	private UserService userService;

	private TransferService transferService;

	public String getMessage() {
		return message;
	}

	private String message;


	public TransferController(UserService userService, TransferService transferService){
		this.userService = userService;
		this.transferService = transferService;
	}

	@GetMapping("/user/transfer")
	public String transfer(Model model) {

		TransferDto transfer = new TransferDto();
		model.addAttribute("transfer", transfer);

		List<TransferDto> transfersDone = transferService.findAllUsersTransfers(getLoggedUser());

/*		List<Transfer> transfersDone = getLoggedUser().getTransfers_done();
		System.out.print("liste transferts : ");
		getLoggedUser().printTransfersDone();
		// Plutôt chercher avec findAllTranserts*/

		model.addAttribute("transfersDone", transfersDone);

		List<User> connections = getLoggedUser().getContacts();
		model.addAttribute("connections", connections);

		model.addAttribute("message", message);

	return "transfer";
	}

	@PostMapping("/user/transfer/pay")
	public String addTransfer(@ModelAttribute("transfer") TransferDto transferDto,
	                          BindingResult result,
	                          Model model) {

		User loggedUser = getLoggedUser();

		if (loggedUser == null) {
			message = "Logged user not found, the transfer was not effected";
			return "redirect:/user/transfer?error";
		}

		try{

			Integer idLoggedUser = loggedUser.getId();
			transferDto.setDebtor(idLoggedUser);

			transferDto.setCreditor(userService.findUserByEmail(transferDto.getCreditorEmail()).getId());

			transferService.isAccountBalanceSufficient(transferDto, loggedUser.getAccountBalance());

			transferService.saveTransfer(transferDto);
			userService.addTransfer(transferDto);

			message = "The transfer was successfully done ! You have now "  + loggedUser.getAccountBalance()+  " € on your account";

			return "redirect:/user/transfer?success";

		} catch (Exception exception){

			if(exception instanceof InsufficientBalanceException) {
				message = "Your balance account is insufficient, the transfer was not effected. You can send a maximum of " + loggedUser.getAccountBalance()/1.05 + " €";
			} else {
				message = "Unknown error, the transfer was not effected";
			}

			return "redirect:/user/transfer?error";
		}
	}

	private User getLoggedUser() {
		// TODO reporter cette méthode dans les autres contrôleurs
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userService.findUserByEmail(authentication == null ? "" : authentication.getName());
	}

}
