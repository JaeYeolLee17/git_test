package com.e4motion.challenge.data.provider.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.data.provider.dto.LoginDto;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.UnauthorizedException;
import com.e4motion.common.exception.customexception.UserNotFoundException;
import com.e4motion.common.utils.JsonHelper;

@SpringBootTest
@ActiveProfiles("unittest")
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureMockMvc
public class AuthControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void loginWithAdminUser() throws Exception {
		assertLogin("admin", "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0",
				HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	public void loginWithDataUser() throws Exception {
		assertLogin("algorithm", "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0",
				HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	public void loginWithIncorrectPassword() throws Exception {
		assertLogin("simulator", "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3------",
				HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	public void loginWithNoexistentUser() throws Exception {
		assertLogin("anonymous", "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0",
				HttpStatus.NOT_FOUND, Response.FAIL, UserNotFoundException.CODE);
	}
	
	private void assertLogin(String userId, String password, 
			HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		String uri = "/v1/login";
		
		LoginDto loginDto = LoginDto.builder()
				.userId(userId)
				.password(password)
				.build();
	    
	    mockMvc.perform(MockMvcRequestBuilders.post(uri)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(JsonHelper.toJson(loginDto)))
	    .andExpect(result -> {
	    		MockHttpServletResponse response = result.getResponse();
	    		assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    		assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedCode != null) {
	    			assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    });
	}

}
