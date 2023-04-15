package com.paymybuddy.paymybuddyapp.controller;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.TransferService;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransferController {

	private UserService userService;

	private TransferService transferService;

	public TransferController(UserService userService, TransferService transferService){
		this.userService = userService;
		this.transferService = transferService;
	}

	@GetMapping("/user/transfer")
	public String transfer(Model model) {
		TransferDto transfer = new TransferDto();
		model.addAttribute("transfer", transfer);
		return "transfer";
	}

	@PostMapping("/user/transfer/pay")
	public String addTransfer(@ModelAttribute("transfer") TransferDto transferDto,
	                          BindingResult result,
	                          Model model) {
		User loggedUser = getLoggedUser();
		Integer idLoggedUser = loggedUser.getId();
		transferDto.setDebtor(idLoggedUser);
		transferService.saveTransfer(transferDto);
		return "redirect:/transfer";
	}

	private User getLoggedUser() {
		User loggedUser = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return loggedUser;
	}

}
