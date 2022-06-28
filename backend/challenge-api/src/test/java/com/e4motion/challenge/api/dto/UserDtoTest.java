package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestHelper;
import com.e4motion.challenge.api.security.SecurityHelper;
import com.e4motion.challenge.api.service.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@AutoConfigureMockMvc
class UserDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    SecurityHelper securityHelper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk_Create() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk_Update() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUserId());

        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUsername_Create() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();

        userDto.setUsername(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUsername_Update() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUserId());

        userUpdateDto.setUsername(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setUsername("");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername(" ");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePassword_Create() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();

        userDto.setPassword(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPassword("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPassword(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPassword("challenge");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPassword("challenge1123");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPassword("1123!@1123");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPassword("cha112!");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePassword_Update() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUserId());

        String oldPassword = userUpdateDto.getOldPassword();

        // old password
        userUpdateDto.setOldPassword(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setOldPassword("");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setOldPassword(" ");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setOldPassword(oldPassword);

        // new password
        userUpdateDto.setNewPassword(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setNewPassword("");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword(" ");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword("challenge");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword("challenge1123");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword("1123!@1123");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword("cha112!");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateNickname_Create() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

        userDto.setNickname(null);
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setNickname("");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setNickname(" ");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateNickname_Update() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUserId());

        userUpdateDto.setNickname(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setNickname("");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setNickname(" ");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateEmail_Create() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

        userDto.setEmail(null);
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setEmail("");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setEmail(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("user1@email...");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("user1email.com");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("user1@emailcom");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateEmail_Update() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUserId());

        userUpdateDto.setEmail(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setEmail("");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setEmail(" ");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername("user1@email...");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername("user1email.com");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername("user1@emailcom");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePhone_Create() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

        userDto.setPhone(null);
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setPhone("");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setPhone(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPhone("010-2222-3333");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPhone("01022223333-");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setPhone("01022223");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePhone_Update() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUserId());

        userUpdateDto.setPhone(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setPhone("");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setPhone(" ");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setPhone("010-2222-3333");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setPhone("01022223333-");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setPhone("01022223");
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateAuthority_Create() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();

        userDto.setAuthority(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateAuthority_Update() throws Exception {

        UserDto userDto = TestHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUserId());

        userUpdateDto.setAuthority(null);
        assertUpdate(userDto.getUserId(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
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

    private void assertUpdate(Long userId, UserUpdateDto userUpdateDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/user/" + userId;

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
}