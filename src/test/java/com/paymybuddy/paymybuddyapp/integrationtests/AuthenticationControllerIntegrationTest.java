package com.paymybuddy.paymybuddyapp.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Path;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "toto@toto.com")
public class AuthenticationControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	void setUp(){
/*		mockMvc = MockMvcBuilder
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();*/
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

}
