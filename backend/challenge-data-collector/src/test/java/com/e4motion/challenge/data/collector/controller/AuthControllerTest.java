package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.data.collector.dto.CameraLoginDto;
import com.e4motion.challenge.data.collector.security.CustomUser;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.CameraNotFoundException;
import com.e4motion.common.exception.customexception.UnauthorizedException;
import com.e4motion.common.utils.JsonHelper;
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
	public void login() throws Exception {
	
		String cameraId = "C0001";
		String password = "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0";
		boolean settingsUpdated = false;
		
		doReturn(getUserDetails(cameraId, password, settingsUpdated)).when(userDetailsService).loadUserByUsername(cameraId);
		
		assertLogin(cameraId, password, settingsUpdated, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	public void loginSettingsUpdatedTrue() throws Exception {

		String cameraId = "C0002";
		String password = "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0";
		boolean settingsUpdated = true;

		doReturn(getUserDetails(cameraId, password, settingsUpdated)).when(userDetailsService).loadUserByUsername(cameraId);

		assertLogin(cameraId, password, settingsUpdated, HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	public void loginWithIncorrectPassword() throws Exception {
		
		String cameraId = "C0002";
		String password = "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0";

		doReturn(getUserDetails(cameraId, password, false)).when(userDetailsService).loadUserByUsername(cameraId);
		
		assertLogin(cameraId, "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3------", false,	// Invalid password
				HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.INVALID_PASSWORD);
	}
	
	@Test
	public void loginWithNonexistentCamera() throws Exception {
		String cameraId = "C0100";
		String password = "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0";
		
		doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_ID)).when(userDetailsService).loadUserByUsername(cameraId);
		
		assertLogin(cameraId, password, false, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_ID);
	}
	
	private void assertLogin(String cameraId, String password, boolean expectedSettingsUpdated,
			HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {
		
		String uri = "/v1/camera/login";
		
		CameraLoginDto loginDto = CameraLoginDto.builder()
				.cameraId(cameraId)
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
	
	private UserDetails getUserDetails(String cameraId, String password, boolean settingsUpdated) {
		Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(AuthorityName.ROLE_CAMERA.toString()));
		UserDetails userDetails = new CustomUser(cameraId, 
				passwordEncoder.encode(password),
				settingsUpdated,
				grantedAuthorities);
		return userDetails;
	}

}
