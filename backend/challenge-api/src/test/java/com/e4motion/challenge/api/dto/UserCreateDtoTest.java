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
class UserCreateDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

        doReturn(userDto).when(userService).create(any());

        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUsername() throws Exception {

        UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

        userCreateDto.setUsername(null);
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setUsername("");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setUsername(" ");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setUsername("Too long username exceed length 32");   // 34 length
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePassword() throws Exception {

        UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

        userCreateDto.setPassword(null);
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPassword("");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPassword(" ");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPassword("challenge");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPassword("challenge1123");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPassword("1123!@1123");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPassword("cha112!");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateNickname() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

        doReturn(userDto).when(userService).create(any());

        userCreateDto.setNickname(null);
        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);

        userCreateDto.setNickname("");
        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);

        userCreateDto.setNickname(" ");
        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateEmail() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

        doReturn(userDto).when(userService).create(any());

        userCreateDto.setEmail(null);
        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);

        userCreateDto.setEmail("");
        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);

        userCreateDto.setEmail(" ");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setEmail("user1@email...");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setEmail("user1email.com");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setEmail("user1@emailcom");    // This is ok!!
        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);

        userCreateDto.setEmail("user1-that-has-too-long-email-0123456789-0123456789-0123456789@email-domain-that-has-too-long-email-0123456789-0123456789-012.com");  // length 129
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePhone() throws Exception {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

        doReturn(userDto).when(userService).create(any());

        userCreateDto.setPhone(null);
        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);

        userCreateDto.setPhone("");
        assertCreate(userCreateDto, HttpStatus.OK, Response.OK, null, null);

        userCreateDto.setPhone(" ");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPhone("010-2222-3333");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPhone("01022223333-");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        userCreateDto.setPhone("01022223");
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateAuthority() throws Exception {

        UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

        userCreateDto.setAuthority(null);
        assertCreate(userCreateDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    private void assertCreate(UserCreateDto userCreateDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/user";

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(userCreateDto)))
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