package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.data.collector.HBaseMockTest;
import com.e4motion.challenge.data.collector.dto.CameraLoginDto;
import com.e4motion.challenge.data.collector.security.CustomUser;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
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
public class AuthControllerTest extends HBaseMockTest {
	
	@Autowired 
	MockMvc mockMvc;
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	@MockBean
	UserDetailsService userDetailsService;
	
	@Test
	public void login() throws Exception {
	
		String cameraNo = "C0001";
		String password = "camera12!@";
		boolean settingsUpdated = false;

		doReturn(getUserDetails(cameraNo, password, settingsUpdated)).when(userDetailsService).loadUserByUsername(cameraNo);
		
		assertLogin(cameraNo, password, settingsUpdated, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	public void loginSettingsUpdatedTrue() throws Exception {

		String cameraNo = "C0002";
		String password = "camera12!@";
		boolean settingsUpdated = true;

		doReturn(getUserDetails(cameraNo, password, settingsUpdated)).when(userDetailsService).loadUserByUsername(cameraNo);

		assertLogin(cameraNo, password, settingsUpdated, HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	public void loginWithIncorrectPassword() throws Exception {
		
		String cameraNo = "C0002";
		String password = "camera12!@";
		boolean settingsUpdated = false;

		doReturn(getUserDetails(cameraNo, password, settingsUpdated)).when(userDetailsService).loadUserByUsername(cameraNo);
		
		assertLogin(cameraNo, "camera12------", settingsUpdated,	// Invalid password
				HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.INVALID_PASSWORD);
	}
	
	@Test
	public void loginWithNonexistentCamera() throws Exception {

		String cameraNo = "C0100";
		String password = "camera12!@";
		boolean settingsUpdated = false;

		doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO)).when(userDetailsService).loadUserByUsername(cameraNo);
		
		assertLogin(cameraNo, password, settingsUpdated, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_NO);
	}
	
	private void assertLogin(String cameraNo, String password, boolean expectedSettingsUpdated,
							 HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {
		
		String uri = "/v2/camera/login";
		
		CameraLoginDto loginDto = CameraLoginDto.builder()
				.cameraNo(cameraNo)
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
					 	assertThat(body.get("token")).isNotNull();
						assertThat(body.get("settingsUpdated")).isEqualTo(expectedSettingsUpdated);
					} else {
						assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
					}
				});
	}
	
	private UserDetails getUserDetails(String cameraNo, String password, boolean settingsUpdated) {

		Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(AuthorityName.ROLE_CAMERA.toString()));

		return new CustomUser(cameraNo,
				passwordEncoder.encode(password),
				settingsUpdated,
				grantedAuthorities);
	}
}
