package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.exception.customexception.UserDuplicateException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.security.SecurityHelper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@Autowired 
	MockMvc mockMvc;
	
	@MockBean
	UserService userService;

	@MockBean
	SecurityHelper securityHelper;

	@Test
	public void createWithoutRole() throws Exception {

		assertCreate(TestDataHelper.getUserDto1(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole() throws Exception {

		UserDto userCreateDto = TestDataHelper.getUserDto1();
		UserDto userDto = TestDataHelper.getUserDto1();

		doReturn(userDto).when(userService).create(any());

		assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole_DuplicateUser() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto1();
		doThrow(new UserDuplicateException(UserDuplicateException.USERNAME_ALREADY_EXISTS)).when(userService).create(any());

		assertCreate(userDto, HttpStatus.CONFLICT, Response.FAIL, UserDuplicateException.CODE, UserDuplicateException.USERNAME_ALREADY_EXISTS);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void createWithManagerRole() throws Exception {

		UserDto userCreateDto = TestDataHelper.getUserDto1();
		UserDto userDto = TestDataHelper.getUserDto1();

		doReturn(userDto).when(userService).create(any());

		assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void createWithInaccessibleRoles() throws Exception {

		assertCreate(TestDataHelper.getUserDto1(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void updateWithoutRole() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

		assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

		doReturn(userDto).when(userService).update(any(), any());
		doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

		assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole_NonexistentUser() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

		doThrow(new UserNotFoundException(UserNotFoundException.INVALID_USERNAME)).when(userService).update(any(), any());

		assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.NOT_FOUND, Response.FAIL, UserNotFoundException.CODE, UserNotFoundException.INVALID_USERNAME);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void updateWithManagerRole() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

		doReturn(userDto).when(userService).update(any(), any());
		doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

		assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "USER")
	public void updateWithUserRole() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

		doReturn(userDto).when(userService).update(any(), any());
		doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

		assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

		doThrow(new InaccessibleException(InaccessibleException.ACCESS_DENIED)).when(securityHelper).checkIfLoginUserForRoleUser("other_user");
		assertUpdate("other_user", userUpdateDto, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	@WithMockUser(roles = {"DATA", "CAMERA_ADMIN"})
	public void updateWithInaccessibleRoles() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

		assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void deleteWithoutRole() throws Exception {

		assertDelete(TestDataHelper.getUserDto2().getUsername(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void deleteWithAdminRole() throws Exception {

		assertDelete(TestDataHelper.getUserDto1().getUsername(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getUserDto2().getUsername(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("another_user", HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteWithManagerRole() throws Exception {

		assertDelete(TestDataHelper.getUserDto1().getUsername(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getUserDto2().getUsername(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("another_user", HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void deleteWitInaccessibleRole() throws Exception {

		assertDelete(TestDataHelper.getUserDto2().getUsername(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void getWithoutRole() throws Exception {

		assertGet(TestDataHelper.getUserDto1().getUsername(), true, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto1();
		doReturn(userDto).when(userService).get(userDto.getUsername());
		doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

		assertGet(userDto.getUsername(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole_NonexistentUserId() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto1();
		doReturn(null).when(userService).get(userDto.getUsername());
		doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

		assertGet(userDto.getUsername(), false, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void getWithManagerRole() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto1();
		doReturn(userDto).when(userService).get(userDto.getUsername());
		doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

		assertGet(userDto.getUsername(), true, HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void getWithUserRole() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto1();
		doReturn(userDto).when(userService).get(userDto.getUsername());
		doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

		assertGet(userDto.getUsername(), true, HttpStatus.OK, Response.OK, null, null);

		doThrow(new InaccessibleException(InaccessibleException.ACCESS_DENIED)).when(securityHelper).checkIfLoginUserForRoleUser("other_user");
		assertGet("other_user", true, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	@WithMockUser(roles = {"DATA", "CAMERA_ADMIN"})
	public void getWithInaccessibleRoles() throws Exception {

		assertGet(TestDataHelper.getUserDto1().getUsername(), true, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
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
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void getListWithUserRole() throws Exception {

		assertGetList(HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	private void assertCreate(UserDto userDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {
		
		String uri = "/v2/user";
	    
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
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
	    			}
				});
	}

	private void assertUpdate(String username, UserUpdateDto userUpdateDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/user/" + username;

	    mockMvc.perform(MockMvcRequestBuilders.put(uri)
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonHelper.toJson(userUpdateDto)))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
	    			assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    			Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    			assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    			if (expectedResult.equals(Response.OK)) {
	    				assertThat(body.get("user")).isNotNull();
	    			} else {
	    				assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
	    			}
				});
	}
	
	private void assertDelete(String username, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		doNothing().when(userService).delete(username);
		
		String uri = "/v2/user/" + username;

	    mockMvc.perform(MockMvcRequestBuilders.delete(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
	    			assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    			Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    			assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    			if (!expectedResult.equals(Response.OK)) {
	    				assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
	    			}
	    });
	}

	private void assertGet(String username, boolean userExpected, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/user/" + username;

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
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
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
					}
				});
	}

	private void assertGetList(HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		UserDto userDto1 = TestDataHelper.getUserDto1();
		UserDto userDto2 = TestDataHelper.getUserDto2();

		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto1);
		userDtos.add(userDto2);

		doReturn(userDtos).when(userService).getList();

		String uri = "/v2/users";

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						assertThat(body.get("users")).isNotNull();
					} else {
						assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
					}
				});
	}
}
