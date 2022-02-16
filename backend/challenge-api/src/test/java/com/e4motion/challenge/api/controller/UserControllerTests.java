package com.e4motion.challenge.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.security.JwtAccessDeniedHandler;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.UnauthorizedException;
import com.e4motion.common.utils.JsonHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
//@ActiveProfiles("unittest")
//@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureMockMvc
public class UserControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean UserService userService;

	@Test
	public void createWithoutAuthentication() throws Exception {
		
		assertCreate(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithRoleAdmin() throws Exception {
		
		assertCreate(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void createWithRoleManager() throws Exception {
		
		assertCreate(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void createWithRoleUser() throws Exception {
		
		assertCreate(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	private void assertCreate(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		UserDto userDto = UserDto.builder()
				.userId("user")
				.password("password")
				.username("username")    
				.email("user@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_USER)
				.build();
		
		when(userService.create(userDto)).thenReturn(userDto);
		
		String uri = "/v1/user";
	    
	    mockMvc.perform(MockMvcRequestBuilders.post(uri)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(JsonHelper.toJson(userDto)))
	    .andExpect(result -> {
	    		MockHttpServletResponse response = result.getResponse();
	    		assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    		assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedResult.equals(Response.OK)) {
	    			log.error(body.get("user").toString());
	    		} else {
	    			assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    });
	}
	
	@Test
	public void updateWithoutAuthentication() throws Exception {
		assertUpdate(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithRoleAdmin() throws Exception {
		assertUpdate(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateNonExistUserWithRoleAdmin() throws Exception {
		//assertUpdate(HttpStatus.NOT_FOUND, Response.FAIL, null);	// Nonexistent user.
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void updateWithRoleManager() throws Exception {
		assertUpdate(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void updateWithRoleUser() throws Exception {
		assertUpdate(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	private void assertUpdate(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		UserDto userDto = UserDto.builder()
				.userId("user2")
				.password("password2")
				.username("username2")    
				.email("user2@email...")
				.phone("01044445555")
				.authority(AuthorityName.ROLE_USER)
				.build();
		
		when(userService.update(userDto.getUserId(), userDto)).thenReturn(userDto);
		
		String uri = "/v1/user/" + userDto.getUserId();
	    
	    mockMvc.perform(MockMvcRequestBuilders.put(uri)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(JsonHelper.toJson(userDto)))
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
	
	@Test
	public void deleteWithoutAuthentication() throws Exception {
		assertDelete(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void deleteNonExistUserWithRoleAdmin() throws Exception {
		assertDelete(HttpStatus.OK, Response.OK, null);	// Nonexistent user but ok.
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteWithRoleManager() throws Exception {
		assertDelete(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void delete() throws Exception {
		assertDelete(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	private void assertDelete(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		String userId = "user2";
		
		doNothing().when(userService).delete(userId);
		
		String uri = "/v1/user/" + userId;

	    mockMvc.perform(MockMvcRequestBuilders.delete(uri)
	    		.contentType(MediaType.APPLICATION_JSON))
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
	
	@Test
	public void getListWithoutAuthentication() throws Exception {
		assertGetList(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getListWithRoleAdmin() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void getListWithRoleManager() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void getListWithRoleUser() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null);
	}
	
	private void assertGetList(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		UserDto userDto = UserDto.builder()
				.userId("user")
				.password("password")
				.username("username")    
				.email("user@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_USER)
				.build();
		
		UserDto userDto2= UserDto.builder()
				.userId("user2")
				.password("password2")
				.username("username2")    
				.email("user2@email...")
				.phone("01044445555")
				.authority(AuthorityName.ROLE_USER)
				.build();
		
		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto);
		userDtos.add(userDto2);
		
		String uri = "/v1/users";
	    
		when(userService.getList()).thenReturn(userDtos);
		
	    mockMvc.perform(MockMvcRequestBuilders.get(uri)
	    		.contentType(MediaType.APPLICATION_JSON))
	    .andExpect(result -> {
	    		MockHttpServletResponse response = result.getResponse();
	    		assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    		assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedCode != null) {	// TODO: refactor
	    			assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    	});
	}
	
	@Test
	public void getWithoutAuthentication() throws Exception {
		assertGet(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithRoleAdmin() throws Exception {
		assertGet(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void getWithRoleManager() throws Exception {
		assertGet(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void getWithRoleUser() throws Exception {
		assertGet(HttpStatus.OK, Response.OK, null);
	}
	
	private void assertGet(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		UserDto userDto = UserDto.builder()
				.userId("user")
				.password("password")
				.username("username")    
				.email("user@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_USER)
				.build();
		
		when(userService.get(userDto.getUserId())).thenReturn(userDto);
		
		String uri = "/v1/user/" + userDto.getUserId();
		
	    mockMvc.perform(MockMvcRequestBuilders.get(uri)
	    		.contentType(MediaType.APPLICATION_JSON))
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
