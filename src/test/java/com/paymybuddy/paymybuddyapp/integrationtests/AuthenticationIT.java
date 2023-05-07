package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.controller.AuthenticationController;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.repository.UserRepository;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.IOException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationIT {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	void setUpEach() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
		userRepository.deleteAll();
		;
	}


	/////////////////////////////////////////////////////////////////

	@Test
	void register_IT() throws Exception {

		UserDto userToSave = new UserDto();
		userToSave.setFirstname("firstname");
		userToSave.setLastname("lastname");
		userToSave.setEmail("email@email.com");
		userToSave.setPassword("1234");

		mockMvc.perform(post("/register/save")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("firstname", userToSave.getFirstname())
						.param("lastname", userToSave.getLastname())
						.param("email", userToSave.getEmail())
						.param("password", userToSave.getPassword())
						.with(csrf())
				)
				.andDo(print())
				.andExpect(view().name("redirect:/index?success"));


		User userSaved = userService.findUserByEmail(userToSave.getEmail());

		// THEN
		assertEquals(userToSave.getFirstname(), userSaved.getFirstname());
		assertEquals(userToSave.getLastname(), userSaved.getLastname());
		assertEquals(userToSave.getEmail(), userSaved.getEmail());
		assertEquals(0, userSaved.getAccountBalance());


		UserDto userToSave2 = new UserDto();
		userToSave2.setFirstname("firstname");
		userToSave2.setLastname("lastname");
		userToSave2.setEmail("email2@email.com");
		userToSave2.setPassword("1234");

		mockMvc.perform(post("/register/save")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("firstname", userToSave2.getFirstname())
						.param("lastname", userToSave2.getLastname())
						.param("email", userToSave2.getEmail())
						.param("password", userToSave2.getPassword())
						.with(csrf())
				)
				.andDo(print())
				.andExpect(view().name("redirect:/index?success"));


		User userSaved2 = userService.findUserByEmail(userToSave2.getEmail());

		// THEN
		assertEquals(userToSave2.getFirstname(), userSaved2.getFirstname());
		assertEquals(userToSave2.getLastname(), userSaved2.getLastname());
		assertEquals(userToSave2.getEmail(), userSaved2.getEmail());
		assertEquals(0, userSaved2.getAccountBalance());
/*		mockMvc.perform(post("/register/save")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("firstname", userToSave.getFirstname())
						.param("lastname", userToSave.getLastname())
						.param("email", userToSave.getEmail())
						.param("password", userToSave.getPassword())
						.with(csrf())
				)
				.andDo(print())
				.andExpect(view().name("register"));


		mockMvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("username", userToSave.getEmail())
						.param("password", userToSave.getPassword())
						.with(csrf())
				)
				.andDo(print()).
				andExpect(redirectedUrl("/user/home"))
		;*/

/*		mockMvc.perform(get("/logout").with(csrf()))
				.andDo(print());*/

/*		mockMvc
				.perform(formLogin("/login").user(userToSave.getEmail()).password(userToSave.getPassword()))
				.andDo(print())
				.andExpect(redirectedUrl("/user/home"));*/

	/*	mockMvc.perform(get("/user/home").with(user(userToSave.getEmail())).with(csrf())
				)
				.andDo(print())
				.andExpect(status().isOk());*/

		mockMvc.perform(post("/user/contact/save")
						.with(user(userSaved.getEmail()))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("email", userSaved2.getEmail())
				)
				.andDo(print())
				.andExpect(redirectedUrl("/user/contact?success"));


		mockMvc.perform(post("/user/home/addmoney")
						.with(user(userSaved.getEmail()))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("amount", "1000000")
				)
				.andDo(print())
				.andExpect(redirectedUrl("/user/home?successAdd"));

		mockMvc.perform(post("/user/transfer/pay")
						.with(user(userSaved.getEmail()))
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("creditorEmail", userSaved2.getEmail())
						.param("amount", "1000")
						.param("reason", "because I can")
				)
				.andDo(print());


		userSaved = userService.findUserByEmail(userToSave.getEmail());
		userSaved2 = userService.findUserByEmail(userToSave2.getEmail());

		// THEN
		assertEquals(998995, userSaved.getAccountBalance());
		assertEquals(1000, userSaved2.getAccountBalance());

	}

	//	@Test
	//	void login_IT(){
	//
	//		// GIVEN
	//		// WHEN
	//		// Requête page login avec infos de connection
	//
	//		// THEN
	//		// est authentifié ?
	//		// loggedUser = ... ?
	//
	//		fail("TODO");
	//	}
	//
	//	@Test
	//	void logoff_IT() {
	//
	//		// GIVEN
	//		// WHEN
	//		// Requête page home/contact/transfer/profile
	//
	//		// THEN
	//		// n'est plus authentifié ?
	//		// loggedUser = ... ?
	//
	//		fail("TODO");
	//
	//	}


	////////////////////////////////////////////////////////////////////////////////


	//	@Test
	//	void indexPage() throws Exception {
	//
	///*		mockMvc.perform(get("/index"))
	//				.andDo(print())
	//				.andExpect(view().name("index"));
	//		// Marche si je renomme mon URI ou ma page... (Circular view path...)*/
	//
	//	}
	//
/*	@Test
	void registrationPage() throws Exception {

		mockMvc.perform(get("/register"))
				.andDo(print())
				.andExpect(view().name("register"));
		// Marche si je renomme mon URI ou ma page... (Circular view path...)

	}*/
	//
	//	@Test
	//	void registration() throws Exception {
	//
	//		UserDto userDto = new UserDto();
	//		userDto.setEmail("TOTO@email.com");
	//		userDto.setFirstname("Sofirstname");
	//		userDto.setLastname("LAUlastname");
	//		userDto.setPassword("password");
	//
	//		mockMvc.perform(post("/register/save")
	//						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	////								.sessionAttr("userDto", userDto)
	////								.sessionAttr("firstname", userDto.getFirstname())
	////								.sessionAttr("lastname", userDto.getLastname())
	////								.sessionAttr("email", userDto.getEmail())
	////								.sessionAttr("password", userDto.getPassword())
	// 						.param("firstname", userDto.getFirstname())
	//						.param("lastname", userDto.getLastname())
	//						.param("email", userDto.getEmail())
	//						.param("password", userDto.getPassword())
	//				)
	////				.with(csrf())
	//				.andDo(print())
	//				.andExpect(view().name("redirect:/index?success"));
	//
	//	}
	//
	//	@Test
	//	void test() throws IOException {
	////		mockMvc.perform(get("/login"))).andDo(print()).andExpect(status().isOk());
	//	}
	//
	//	@Test
	//	void userLoginFailed() throws Exception {
	///*		mockMvc.perform(formLogin("/login"))
	//				.user("user")
	//				.password("wrongpassword"))
	//		.andExpect(unauthenticated());*/
	//	}
	//
	//	@Test
	//	void registration_Test() throws Exception {
	//		//		mockMvc.perform(post("/register/save"));
	//		//
	//		//		UserDto userDto = new UserDto();
	//		//		userDto.setId(1);
	//		//		userDto.setFirstname("firstname");
	//		//		userDto.setLastname("lastname");
	//		//		userDto.setEmail("email@email.com");
	//		//		userDto.setPassword("password");
	//		//		userDto.setAccountBalance(100.00);
	//		//
	//		//		mockMvc.perform(MockMvcRequestBuilders
	//		//				.post("/register/save")
	//		//						.param("userDto", userDto))
	//		//				.content(new UserDto())
	//		//				.andExpect(status().isCreated());
	//		//		)
	//	}
}
