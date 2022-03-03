package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.exception.customexception.UserDuplicateException;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@Autowired 
	MockMvc mockMvc;
	
	@MockBean
	UserService userService;

	@Test
	public void getWithoutRole() throws Exception {
		assertGet(getUserDto1().getUserId(), true, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole() throws Exception {
		UserDto userDto = getUserDto1();
		doReturn(userDto).when(userService).get(userDto.getUserId());

		assertGet(userDto.getUserId(), true, HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void getWithManagerRole() throws Exception {
		UserDto userDto = getUserDto1();
		doReturn(userDto).when(userService).get(userDto.getUserId());

		assertGet(userDto.getUserId(), true, HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void getWithUserRole() throws Exception {
		UserDto userDto = getUserDto1();
		doReturn(userDto).when(userService).get(userDto.getUserId());

		assertGet(userDto.getUserId(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "USER")
	public void getWithUserRoleWithNonexistentUserId() throws Exception {	// Nonexistent user id
		UserDto userDto = getUserDto1();
		doReturn(null).when(userService).get(userDto.getUserId());

		assertGet(userDto.getUserId(), false, HttpStatus.OK, Response.OK, null, null);
	}

	private void assertGet(String userId, boolean userExpected, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v1/user/" + userId;
		
	    mockMvc.perform(MockMvcRequestBuilders.get(uri)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
	    			assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    			Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    			assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    			if (expectedResult.equals(Response.OK)) {
						if (userExpected) {
							assertThat(body.get("user")).isNotNull();
						} else {
							assertThat(body.get("user")).isNull();
						}
	    			} else {
	    				assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
	    			}
				});
	}
	
	@Test
	public void getListWithoutRole() throws Exception {
		assertGetList(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getListWithAdminRole() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void getListWithManagerRole() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void getListWithUserRole() throws Exception {
		assertGetList(HttpStatus.OK, Response.OK, null, null);
	}
	
	private void assertGetList(HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {
		
		UserDto userDto1 = getUserDto1();
		UserDto userDto2 = getUserDto2();
		
		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto1);
		userDtos.add(userDto2);
	    
		doReturn(userDtos).when(userService).getList();
		
		String uri = "/v1/users";
		
	    mockMvc.perform(MockMvcRequestBuilders.get(uri)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
	    			assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    			Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    			assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    			if (expectedResult.equals(Response.OK)) {
	    				assertThat(body.get("users")).isNotNull();
	    			} else {
	    				assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
	    			}
				});
	}
	
	@Test
	public void createWithoutRole() throws Exception {
		assertCreate(getUserDto1(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole() throws Exception {
		UserDto userDto = getUserDto1();
		doReturn(userDto).when(userService).create(userDto);

		assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRoleWithDuplicateUser() throws Exception {	// create duplicate user
		UserDto userDto = getUserDto1();
		doThrow(new UserDuplicateException(UserDuplicateException.USER_ID_ALREADY_EXISTS)).when(userService).create(userDto);

		assertCreate(userDto, HttpStatus.CONFLICT, Response.FAIL, UserDuplicateException.CODE, UserDuplicateException.USER_ID_ALREADY_EXISTS);
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void createWithManagerRole() throws Exception {
		assertCreate(getUserDto1(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void createWithUserRole() throws Exception {
		assertCreate(getUserDto1(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	private void assertCreate(UserDto userDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {
		
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
	    				assertThat(body.get("user")).isNotNull();
	    			} else {
	    				assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
	    			}
				});
	}
	
	@Test
	public void updateWithoutRole() throws Exception {
		assertUpdate(getUserDto2(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole() throws Exception {
		UserDto userDto = getUserDto2();
		doReturn(userDto).when(userService).update(userDto.getUserId(), userDto);

		assertUpdate(userDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRoleWithNonexistentUser() throws Exception {	// update nonexistent user
		UserDto userDto = getUserDto2();
		doThrow(new UserNotFoundException(UserNotFoundException.INVALID_USER_ID)).when(userService).update(userDto.getUserId(), userDto);

		assertUpdate(userDto, HttpStatus.NOT_FOUND, Response.FAIL, UserNotFoundException.CODE, UserNotFoundException.INVALID_USER_ID);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void updateWithManagerRole() throws Exception {
		assertUpdate(getUserDto2(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void updateWithUserRole() throws Exception {
		assertUpdate(getUserDto2(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}
	
	private void assertUpdate(UserDto userDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v1/user/" + userDto.getUserId();
	    
	    mockMvc.perform(MockMvcRequestBuilders.put(uri)
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonHelper.toJson(userDto)))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
	    			assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    			Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    			assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    			if (expectedResult.equals(Response.OK)) {
	    				assertThat(body.get("user")).isNotNull();
	    			} else {
	    				assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
	    			}
				});
	}
	
	@Test
	public void deleteWithoutRole() throws Exception {
		assertDelete(getUserDto2().getUserId(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void deleteWithAdminRole() throws Exception {
		assertDelete(getUserDto1().getUserId(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(getUserDto2().getUserId(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("user3", HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteWithManagerRole() throws Exception {
		assertDelete(getUserDto2().getUserId(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void deleteWithUserRole() throws Exception {
		assertDelete(getUserDto2().getUserId(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}
	
	private void assertDelete(String userId, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		doNothing().when(userService).delete(userId);
		
		String uri = "/v1/user/" + userId;

	    mockMvc.perform(MockMvcRequestBuilders.delete(uri)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
	    			assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    			Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    			assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    			if (!expectedResult.equals(Response.OK)) {
	    				assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
	    			}
	    });
	}

	private UserDto getUserDto1() {
		return UserDto.builder()
				.userId("user1")
				.password("password1")
				.username("username1")
				.email("user1@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_USER)
				.build();
	}

	private UserDto getUserDto2() {
		return UserDto.builder()
				.userId("user2")
				.password("password2")
				.username("username2")
				.email("user2@email...")
				.phone("01044445555")
				.authority(AuthorityName.ROLE_USER)
				.build();
	}
}
