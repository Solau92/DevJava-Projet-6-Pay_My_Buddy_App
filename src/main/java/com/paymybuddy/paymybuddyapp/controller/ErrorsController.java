package com.paymybuddy.paymybuddyapp.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorsController implements ErrorController {

	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {

		String errorPage = "error"; // default

/*		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());

			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				errorPage = "error404";

			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				errorPage = "error403";

			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				errorPage = "error";

			}
		}*/
		return errorPage;
	}
}
