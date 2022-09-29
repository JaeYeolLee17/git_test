package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserUpdateDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    SecurityHelper securityHelper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUsername() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

        userUpdateDto.setUsername(null);
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setUsername("");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername(" ");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername("Too long username over length 32-");     // length 33
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername("Long username length 32 12345678");      // length 32
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePassword() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

        String oldPassword = userUpdateDto.getOldPassword();

        // old password
        userUpdateDto.setOldPassword(null);
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setOldPassword("");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setOldPassword(" ");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setOldPassword(oldPassword);

        // new password
        userUpdateDto.setNewPassword(null);
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setNewPassword("");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword(" ");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword("challenge");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword("challenge1123");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword("1123!@1123");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setNewPassword("cha112!");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // length 129
        userUpdateDto.setNewPassword("user12!@1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // length 128
        userUpdateDto.setNewPassword("user12!@123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateName() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

        userUpdateDto.setName(null);
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setName("");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setName(" ");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setName("Long name exceed length 32-------");     // length 33
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setName("Long name length 32 123456789012");      // length 32
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateEmail() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

        userUpdateDto.setEmail(null);
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setEmail("");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setEmail(" ");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername("user1@email...");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername("user1email.com");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setUsername("user1@emailcom");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // length 129
        userUpdateDto.setEmail("user1-that-has-too-long-email-0123456789-0123456789-0123456789@email-domain-that-has-too-long-email-0123456789-0123456789-012.com");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // length 128
        userUpdateDto.setEmail("user1-that-has-too-long-email-0123456789-0123456789-0123456789@email-domain-that-has-too-long-email-0123456789-0123456789-01.com");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePhone() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

        userUpdateDto.setPhone(null);
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setPhone("");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);

        userUpdateDto.setPhone(" ");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setPhone("010-2222-3333");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setPhone("01022223333-");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userUpdateDto.setPhone("01022223");
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateAuthority() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

        doReturn(userDto).when(userService).update(any(), any());
        doNothing().when(securityHelper).checkIfLoginUserForRoleUser(userDto.getUsername());

        userUpdateDto.setAuthority(null);
        assertUpdate(userDto.getUsername(), userUpdateDto, HttpStatus.OK, Response.OK, null, null);
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
}