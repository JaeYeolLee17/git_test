package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUsername() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

        userDto.setUsername(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("Too long username exceed length 32");  // length 34
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setUsername("Long username length 32 12345678");    // length 32
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePassword() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

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

        // length 129
        userDto.setPassword("user12!@1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // length 128
        userDto.setPassword("user12!@123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateName() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

        userDto.setName(null);
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setName("");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setName(" ");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setName("Long name exceed length 32-------");   // length 33
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setName("Long name length 32 123456789012");    // length 32
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateEmail() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();

        doReturn(userDto).when(userService).create(any());

        userDto.setEmail(null);
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setEmail("");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        userDto.setEmail(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setEmail("user1@email...");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setEmail("user1email.com");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userDto.setEmail("user1@emailcom");    // This is ok!!
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);

        // length 129
        userDto.setEmail("user1-that-has-too-long-email-0123456789-0123456789-0123456789@email-domain-that-has-too-long-email-0123456789-0123456789-012.com");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // length 128
        userDto.setEmail("user1-that-has-too-long-email-0123456789-0123456789-0123456789@email-domain-that-has-too-long-email-0123456789-0123456789-01.com");
        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePhone() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();

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
    public void validateAuthority() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();

        userDto.setAuthority(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
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
}