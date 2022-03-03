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
    public void validateOk() throws Exception {

        UserDto userDto = getGoodUserDto();

        doReturn(userDto).when(userService).create(userDto);

        assertCreate(userDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUserId() throws Exception {

        UserDto userDto = getGoodUserDto();

        userDto.setUserId(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        userDto.setUserId("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        userDto.setUserId(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validatePassword() throws Exception {

        UserDto userDto = getGoodUserDto();

        userDto.setPassword(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        userDto.setPassword("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        userDto.setPassword(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUsername() throws Exception {

        UserDto userDto = getGoodUserDto();

        userDto.setUsername(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        userDto.setUsername("");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        userDto.setUsername(" ");
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateAuthority() throws Exception {

        UserDto userDto = getGoodUserDto();

        userDto.setAuthority(null);
        assertCreate(userDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    private UserDto getGoodUserDto() {
        return UserDto.builder()
                .userId("user1")
                .password("password1")
                .username("username1")
                .email(null)
                .phone(null)
                .authority(AuthorityName.ROLE_USER)
                .build();
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
}