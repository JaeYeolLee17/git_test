package com.e4motion.challenge.data.provider.dto;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.provider.HBaseMockTest;
import com.e4motion.challenge.data.provider.security.CustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@AutoConfigureMockMvc
class LoginDtoTest extends HBaseMockTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    UserDetailsService userDetailsService;

    @Test
    public void validateOk() throws Exception {

        String username = "simulator";
        String password = "challenge12!@";
        AuthorityName authority = AuthorityName.ROLE_DATA;

        doReturn(getUserDetails(username, password, authority)).when(userDetailsService).loadUserByUsername(username);

        assertLogin(username, password, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    public void validateUsername() throws Exception {

        String username = null;
        String password = "challenge12!@";
        assertLogin(username, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        username = "";
        assertLogin(username, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        username = " ";
        assertLogin(username, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        username = "not existent user";
        doThrow(new UserNotFoundException(UserNotFoundException.INVALID_USERNAME)).when(userDetailsService).loadUserByUsername(username);
        assertLogin(username, password, HttpStatus.NOT_FOUND, Response.FAIL, UserNotFoundException.CODE, UserNotFoundException.INVALID_USERNAME);
    }

    @Test
    public void validatePassword() throws Exception {

        String username = "simulator";
        String password = null;
        assertLogin(username, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        password = "";
        assertLogin(username, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        password = " ";
        assertLogin(username, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    private void assertLogin(String username, String password,
                             HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/login";

        LoginDto loginDto = LoginDto.builder()
                .username(username)
                .password(password)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(loginDto)))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.get("user")).isNotNull();
                        assertThat(body.get("token")).isNotNull();
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }

    private UserDetails getUserDetails(String username, String password, AuthorityName authority) {
        Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(authority.toString()));
        UserDetails userDetails = new CustomUser(1L,
                username,
                passwordEncoder.encode(password),
                null,
                null,
                null,
                true,
                grantedAuthorities);
        return userDetails;
    }
}