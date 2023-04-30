package com.paymybuddy.paymybuddyapp.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ErrorsController implements ErrorController {

	private String message;

	@GetMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			message = "Error " + status;
		}

		model.addAttribute("message", message);

		log.info("Error " + status + " page");

		return "error";
	}

}
