package com.paymybuddy.paymybuddyapp.integrationtests;

import com.paymybuddy.paymybuddyapp.controller.AuthenticationController;
import com.paymybuddy.paymybuddyapp.dto.UserDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.IOException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "toto@toto.com")
class AuthenticationIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	UserService userService;

	@BeforeEach
	void setUp(){
/*		mockMvc = MockMvcBuilder
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();*/
		mockMvc = MockMvcBuilders.standaloneSetup(new AuthenticationController(userService)).build();
	}


	/////////////////////////////////////////////////////////////////

	@Test
	void register_IT() {

		UserDto userToSave = new UserDto();
		userToSave.setFirstname("firstname");
		userToSave.setLastname("lastname");
		userToSave.setEmail("email@email.com");
		userToSave.setPassword("1234");

		// GIVEN
		// Requête post page register avec les infos

		// WHEN
		User userSaved = userService.findUserByEmail(userToSave.getEmail());

		// THEN
		assertEquals(userToSave.getFirstname(), userSaved.getFirstname());
		assertEquals(userToSave.getLastname(), userSaved.getLastname());
		assertEquals(userToSave.getEmail(), userSaved.getEmail());
		assertEquals(userToSave.getPassword(), userSaved.getPassword());
		assertEquals(0, userSaved.getAccountBalance());

	}

	@Test
	void login_IT(){

		// GIVEN
		// WHEN
		// Requête page login avec infos de connection

		// THEN
		// est authentifié ?
		// loggedUser = ... ?

	}

	@Test
	void logoff_IT() {

		// GIVEN
		// WHEN
		// Requête page home/contact/transfer/profile

		// THEN
		// n'est plus authentifié ?
		// loggedUser = ... ?
	}


	////////////////////////////////////////////////////////////////////////////////



	@Test
	void indexPage() throws Exception {

		mockMvc.perform(get("/index"))
				.andDo(print())
				.andExpect(view().name("index"));
		// Marche si je renomme mon URI ou ma page... (Circular view path...)

	}

	@Test
	void registrationPage() throws Exception {

		mockMvc.perform(get("/register"))
				.andDo(print())
				.andExpect(view().name("register"));
		// Marche si je renomme mon URI ou ma page... (Circular view path...)

	}

	@Test
	void registration() throws Exception {

		UserDto userDto = new UserDto();
		userDto.setEmail("solouser@email.com");
		userDto.setFirstname("Sofirstname");
		userDto.setLastname("LAUlastname");
		userDto.setPassword("password");

		mockMvc.perform(post("/register/save")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//								.sessionAttr("userDto", userDto)
//								.sessionAttr("firstname", userDto.getFirstname())
//								.sessionAttr("lastname", userDto.getLastname())
//								.sessionAttr("email", userDto.getEmail())
//								.sessionAttr("password", userDto.getPassword())
 						.param("firstname", userDto.getFirstname())
						.param("lastname", userDto.getLastname())
						.param("email", userDto.getEmail())
						.param("password", userDto.getPassword())
				)
//				.with(csrf())
				.andDo(print())
				.andExpect(view().name("redirect:/index?success"));

	}

	@Test
	void test() throws IOException {
//		mockMvc.perform(get("/login"))).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void userLoginFailed() throws Exception {
/*		mockMvc.perform(formLogin("/login"))
				.user("user")
				.password("wrongpassword"))
		.andExpect(unauthenticated());*/
	}

	@Test
	void registration_Test() throws Exception {
		//		mockMvc.perform(post("/register/save"));
		//
		//		UserDto userDto = new UserDto();
		//		userDto.setId(1);
		//		userDto.setFirstname("firstname");
		//		userDto.setLastname("lastname");
		//		userDto.setEmail("email@email.com");
		//		userDto.setPassword("password");
		//		userDto.setAccountBalance(100.00);
		//
		//		mockMvc.perform(MockMvcRequestBuilders
		//				.post("/register/save")
		//						.param("userDto", userDto))
		//				.content(new UserDto())
		//				.andExpect(status().isCreated());
		//		)
	}
}
