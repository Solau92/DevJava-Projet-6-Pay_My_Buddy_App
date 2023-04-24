package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.service.TransferService;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TransferController {

	private UserService userService;

	private TransferService transferService;

	private String errorMessage;


	public TransferController(UserService userService, TransferService transferService){
		this.userService = userService;
		this.transferService = transferService;
	}

	@GetMapping("/user/transfer")
	public String transfer(Model model) {

		TransferDto transfer = new TransferDto();
		model.addAttribute("transfer", transfer);

		List<TransferDto> transfersDone = transferService.findAllUsersTransfers(getLoggedUser());

		System.out.println("Liste transferts : ");
			int compteur = 0;
			for (TransferDto t : transfersDone) {
				System.out.println(compteur + ") amount : " + t.getAmount() + " reason : " + t.getReason() + " - ");
				compteur++;
			}

/*		List<Transfer> transfersDone = getLoggedUser().getTransfers_done();
		System.out.print("liste transferts : ");
		getLoggedUser().printTransfersDone();
		// Plutôt chercher avec findAllTranserts*/

		model.addAttribute("transfersDone", transfersDone);

		List<User> connections = getLoggedUser().getContacts();
		model.addAttribute("connections", connections);

		model.addAttribute("message", errorMessage);

	return "transfer";
	}

	@PostMapping("/user/transfer/pay")
	public String addTransfer(@ModelAttribute("transfer") TransferDto transferDto,
	                          BindingResult result,
	                          Model model) {

		try{
			// Erreurs :
			// crédit insuffisant
			//
			User loggedUser = getLoggedUser();
			Integer idLoggedUser = loggedUser.getId();
			transferDto.setDebtor(idLoggedUser);

			transferDto.setCreditor(userService.findUserByEmail(transferDto.getCreditorEmail()).getId());

			if(transferDto.getAmount()*1.05 > loggedUser.getAccountBalance()) {
				throw new InsufficientBalanceException();
			}
			transferService.saveTransfer(transferDto);
			userService.addTransfer(transferDto);

			return "redirect:/user/transfer?success";

		} catch (Exception exception){

			if(exception instanceof InsufficientBalanceException) {
				errorMessage = "Your balance account is insufficient, the transfer was not effected";
			} else {
				errorMessage = "Unknown error, the transfer was not effected";
			}

			return "redirect:/user/transfer?error";
		}
	}

	private User getLoggedUser() {
		User loggedUser = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return loggedUser;
	}

}
