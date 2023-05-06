package com.paymybuddy.paymybuddyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Acts as a main class for Spring Boot application : bootstrapes and launches the Spring application
 * To achieve this, automatically creates the ApplicationContext, scan the configuration classes
 */
@SpringBootApplication
public class PayMyBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

}
