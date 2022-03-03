package com.e4motion.challenge.data.provider.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.data.provider.dto.LoginDto;
import com.e4motion.challenge.data.provider.security.CustomUser;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;
import com.e4motion.challenge.common.utils.JsonHelper;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
	
	@Autowired 
	MockMvc mockMvc;
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	@MockBean
	UserDetailsService userDetailsService;
	
	@Test
	public void loginWithAdminUser() throws Exception {
		
		String userId = "admin";
		String password = "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0";
		AuthorityName authority = AuthorityName.ROLE_ADMIN;
		
		doReturn(getUserDetails(userId, password, authority)).when(userDetailsService).loadUserByUsername(userId);
		
		assertLogin(userId, password, HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	public void loginWithDataUser() throws Exception {
		
		String userId = "algorithm";
		String password = "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0";
		AuthorityName authority = AuthorityName.ROLE_DATA;
		
		doReturn(getUserDetails(userId, password, authority)).when(userDetailsService).loadUserByUsername(userId);
		
		assertLogin(userId, password, HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	public void loginWithIncorrectPassword() throws Exception {
		
		String userId = "simulator";
		String password = "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0";
		AuthorityName authority = AuthorityName.ROLE_DATA;
		
		doReturn(getUserDetails(userId, password, authority)).when(userDetailsService).loadUserByUsername(userId);
		
		assertLogin(userId, "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3------",	 	// Invalid password
				HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.INVALID_PASSWORD);
	}
	
	@Test
	public void loginWithNonexistentUser() throws Exception {
		
		String userId = "anonymous";
		String password = "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0";
		AuthorityName authority = AuthorityName.ROLE_USER;
		
		doThrow(new UserNotFoundException(UserNotFoundException.INVALID_USER_ID)).when(userDetailsService).loadUserByUsername(userId);
		
		assertLogin(userId, password, HttpStatus.NOT_FOUND, Response.FAIL, UserNotFoundException.CODE, UserNotFoundException.INVALID_USER_ID);
	}
	
	private void assertLogin(String userId, String password,
			HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {
		
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
	    		if (expectedResult.equals(Response.OK)) {
	    			assertThat(body.get("user")).isNotNull();
	    			assertThat(body.get("token")).isNotNull();
	    		} else {
	    			assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
					assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
	    		}
	    });
	}
	
	private UserDetails getUserDetails(String userId, String password, AuthorityName authority) {
		Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(authority.toString()));
		UserDetails userDetails = new CustomUser(userId, 
				passwordEncoder.encode(password),
				"username",
				"email",
				"phone",
				grantedAuthorities);
		return userDetails;
	}
}
