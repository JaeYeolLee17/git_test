package com.e4motion.challenge.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.security.JwtAccessDeniedHandler;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.UnauthorizedException;
import com.e4motion.common.utils.TestUtils;

@SpringBootTest
@ActiveProfiles("unittest")
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureMockMvc
public class UserControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void createWithoutAuthentication() throws Exception {
		assertCreate(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	public void createWithRoleAdmin() throws Exception {
		assertCreate(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "MANAGER")
	public void createWithRoleManager() throws Exception {
		assertCreate(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void createWithRoleUser() throws Exception {
		assertCreate(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	private void assertCreate(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		String uri = "/v1/user";
		
		String userId = "user";
		String password = "password";
		String username = "username";
		String email = "user@email...";
		String phone = "01022223333";
		AuthorityName authority = AuthorityName.ROLE_USER;
		
		UserDto userDto = UserDto.builder()
				.userId(userId)
				.password(password)
				.username(username)    
				.email(email)
				.phone(phone)
				.authority(authority)
				.build();
	    
	    mockMvc.perform(MockMvcRequestBuilders.post(uri)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(TestUtils.getJsonContent(userDto)))
	    .andExpect(result -> {
	    		MockHttpServletResponse res = result.getResponse();
	    		assertThat(res.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response response = TestUtils.getResponse(res.getContentAsString());
	    		assertThat(response.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedCode != null) {
	    			assertThat(response.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    });
	}
	
	@Test
	public void updateWithoutAuthentication() throws Exception {
		assertUpdate(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	public void updateNonExistUserWithRoleAdmin() throws Exception {
		assertUpdate(HttpStatus.NOT_FOUND, Response.FAIL, null);	// Nonexistent user.
	}
	
	@Test
	@WithMockUser(username = "user", roles = "MANAGER")
	public void updateWithRoleManager() throws Exception {
		assertUpdate(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void updateWithRoleUser() throws Exception {
		assertUpdate(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	private void assertUpdate(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		String userId = "user2";
		String password = "password2";
		String username = "username2";
		String email = "user2@email...";
		String phone = "01044445555";
		AuthorityName authority = AuthorityName.ROLE_USER;
		
		String uri = "/v1/user/" + userId;
		
		UserDto userDto = UserDto.builder()
				.userId(userId)
				.password(password)
				.username(username)    
				.email(email)
				.phone(phone)
				.authority(authority)
				.build();
	    
	    mockMvc.perform(MockMvcRequestBuilders.put(uri)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(TestUtils.getJsonContent(userDto)))
	    .andExpect(result -> {
	    		MockHttpServletResponse res = result.getResponse();
	    		assertThat(res.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response response = TestUtils.getResponse(res.getContentAsString());
	    		assertThat(response.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedCode != null) {
	    			assertThat(response.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    });
	}
	
	@Test
	public void deleteWithoutAuthentication() throws Exception {
		assertDelete(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	public void deleteNonExistUserWithRoleAdmin() throws Exception {
		assertDelete(HttpStatus.OK, Response.OK, null);	// Nonexistent user but ok.
	}
	
	@Test
	@WithMockUser(username = "user", roles = "MANAGER")
	public void deleteWithRoleManager() throws Exception {
		assertDelete(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void delete() throws Exception {
		assertDelete(HttpStatus.FORBIDDEN, Response.FAIL, JwtAccessDeniedHandler.CODE);
	}
	
	private void assertDelete(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		String userId = "user2";
		
		String uri = "/v1/user/" + userId;
	    
	    mockMvc.perform(MockMvcRequestBuilders.delete(uri)
	    		.contentType(MediaType.APPLICATION_JSON))
	    .andExpect(result -> {
	    		MockHttpServletResponse res = result.getResponse();
	    		assertThat(res.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response response = TestUtils.getResponse(res.getContentAsString());
	    		assertThat(response.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedCode != null) {
	    			assertThat(response.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    });
	}
	
	@Test
	public void getListWithoutAuthentication() throws Exception {
		assertGetList(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	public void getListWithRoleAdmin() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "MANAGER")
	public void getListWithRoleManager() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void getListWithRoleUser() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null);
	}
	
	private void assertGetList(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		String uri = "/v1/users";
	    
	    mockMvc.perform(MockMvcRequestBuilders.get(uri)
	    		.contentType(MediaType.APPLICATION_JSON))
	    .andExpect(result -> {
	    		MockHttpServletResponse response = result.getResponse();
	    		assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response body = TestUtils.getResponse(response.getContentAsString());
	    		assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedCode != null) {
	    			assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    	});
	}
	
	@Test
	public void getWithoutAuthentication() throws Exception {
		assertGet(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	public void getWithRoleAdmin() throws Exception {
		assertGet(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "MANAGER")
	public void getWithRoleManager() throws Exception {
		assertGet(HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void getWithRoleUser() throws Exception {
		assertGet(HttpStatus.OK, Response.OK, null);
	}
	
	private void assertGet(HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		String uri = "/v1/user/admin";
	    
	    mockMvc.perform(MockMvcRequestBuilders.get(uri)
	    		.contentType(MediaType.APPLICATION_JSON))
	    .andExpect(result -> {
	    		MockHttpServletResponse response = result.getResponse();
	    		assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response body = TestUtils.getResponse(response.getContentAsString());
	    		assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedCode != null) {
	    			assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    	});
	}
	
}
