package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@AutoConfigureMockMvc
class UserDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk_Create() throws Exception {

        UserDto userDto = getGoodUserDto();

        doReturn(userDto).when(userService).create(userDto);

        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk_Update() throws Exception {

        UserDto userDto = getGoodUserDto();
        UserUpdateDto userUpdateDto = getGoodUserUpdateDto();

        doReturn(userDto).when(userService).update(userDto.getUserId(), userUpdateDto);

        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUserId_Create() throws Exception {

        UserDto userDto = getGoodUserDto();

        userDto.setUserId(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUserId("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUserId(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePassword_Create() throws Exception {

        UserDto userDto = getGoodUserDto();

        userDto.setPassword(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPassword("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPassword(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePassword_Update() throws Exception {

        UserDto userDto = getGoodUserDto();
        UserUpdateDto userUpdateDto = getGoodUserUpdateDto();

        doReturn(userDto).when(userService).update(userDto.getUserId(), userUpdateDto);

        userUpdateDto.setPassword(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setPassword("");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setPassword(" ");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUsername_Create() throws Exception {

        UserDto userDto = getGoodUserDto();

        userDto.setUsername(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE,null);

        userDto.setUsername(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUsername_Update() throws Exception {

        UserDto userDto = getGoodUserDto();
        UserUpdateDto userUpdateDto = getGoodUserUpdateDto();

        doReturn(userDto).when(userService).update(userDto.getUserId(), userUpdateDto);

        userUpdateDto.setUsername(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setUsername("");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE,null);

        userUpdateDto.setUsername(" ");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateEmail_Create_Update() throws Exception {

        UserDto userDto = getGoodUserDto();

        doReturn(userDto).when(userService).create(userDto);

        userDto.setEmail(null);
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setEmail("");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setEmail(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("user1@email...");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePhone_Create_Update() throws Exception {

        UserDto userDto = getGoodUserDto();

        doReturn(userDto).when(userService).create(userDto);

        userDto.setPhone(null);
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setPhone("");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setPhone(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPhone("010-2222-3333");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateAuthority_Create() throws Exception {

        UserDto userDto = getGoodUserDto();

        userDto.setAuthority(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateAuthority_Update() throws Exception {

        UserDto userDto = getGoodUserDto();
        UserUpdateDto userUpdateDto = getGoodUserUpdateDto();

        doReturn(userDto).when(userService).update(userDto.getUserId(), userUpdateDto);

        userUpdateDto.setAuthority(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
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
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }

    private void assertUpdate(String userId, UserUpdateDto userUpdateDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v1/user/" + userId;

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

    private UserDto getGoodUserDto() {
        return UserDto.builder()
                .userId("user1")
                .password("password1")
                .username("username1")
                .email("user1@email.com")
                .phone("01022223333")
                .authority(AuthorityName.ROLE_USER)
                .build();
    }

    private UserUpdateDto getGoodUserUpdateDto() {
        return UserUpdateDto.builder()
                .password("password-updated")
                .username("username-updated")
                .email("email-updated@email.com")
                .phone("01088889999")
                .authority(AuthorityName.ROLE_ADMIN)
                .build();
    }
}