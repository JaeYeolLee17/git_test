package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.LoginDto;
import com.e4motion.challenge.api.security.CustomUser;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
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

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

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
		
		String username = "admin";
		String password = "challenge1123!";
		AuthorityName authority = AuthorityName.ROLE_ADMIN;
		
		doReturn(getUserDetails(username, password, authority)).when(userDetailsService).loadUserByUsername(username);
		
		assertLogin(username, password, HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	public void loginWithManagerUser() throws Exception {
		
		String username = "manager";
		String password = "challenge1123!";
		AuthorityName authority = AuthorityName.ROLE_MANAGER;
		
		doReturn(getUserDetails(username, password, authority)).when(userDetailsService).loadUserByUsername(username);
		
		assertLogin(username, password, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	public void loginWithUser() throws Exception {

		String username = "user1";
		String password = "challenge12!@";
		AuthorityName authority = AuthorityName.ROLE_USER;

		doReturn(getUserDetails(username, password, authority)).when(userDetailsService).loadUserByUsername(username);

		assertLogin(username, password, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	public void loginWithIncorrectPassword() throws Exception {
		
		String username = "user1";
		String password = "challenge12!@";
		AuthorityName authority = AuthorityName.ROLE_USER;
		
		doReturn(getUserDetails(username, password, authority)).when(userDetailsService).loadUserByUsername(username);
		
		assertLogin(username, "challenge1!----",		// Invalid password
				HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.INVALID_PASSWORD);
	}
	
	@Test
	public void loginWithNonexistentUser() throws Exception {
		
		String username = "anonymous";
		String password = "challenge12!@";
		AuthorityName authority = AuthorityName.ROLE_USER;
		
		doThrow(new UserNotFoundException(UserNotFoundException.INVALID_USERNAME)).when(userDetailsService).loadUserByUsername(username);
		
		assertLogin(username, password, HttpStatus.NOT_FOUND, Response.FAIL, UserNotFoundException.CODE, UserNotFoundException.INVALID_USERNAME);
	}

	@Test
	public void loginWithDisabledUser() throws Exception {

		String username = "user1";
		String password = "challenge12!@";
		AuthorityName authority = AuthorityName.ROLE_USER;

		doReturn(getUserDetails(username, password, authority, false)).when(userDetailsService).loadUserByUsername(username);

		assertLogin(username, password, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.DISABLED_USER);
	}

	private void assertLogin(String username, String password,
			HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/login";

		LoginDto loginDto = LoginDto.builder()
				.username(username)
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
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
	    			}
				});
	}

	private UserDetails getUserDetails(String username, String password, AuthorityName authority) {
		return getUserDetails(username, password, authority, true);
	}

	private UserDetails getUserDetails(String username, String password, AuthorityName authority, Boolean enabled) {
		Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(authority.toString()));
		UserDetails userDetails = new CustomUser(1L,
				username,
				passwordEncoder.encode(password),
				null,
				null,
				null,
				enabled,
				grantedAuthorities);
		return userDetails;
	}
}
