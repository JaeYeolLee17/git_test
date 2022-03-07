package com.e4motion.challenge.data.provider.dto;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.provider.HBaseMockBaseTest;
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
class LoginDtoTest extends HBaseMockBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    UserDetailsService userDetailsService;

    @Test
    public void validateOk() throws Exception {

        LoginDto loginDto = getGoodLoginDto();
        AuthorityName authority = AuthorityName.ROLE_USER;

        doReturn(getUserDetails(loginDto.getUserId(), loginDto.getPassword(), authority)).when(userDetailsService).loadUserByUsername(loginDto.getUserId());

        assertLogin(loginDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    public void validateUserId() throws Exception {

        LoginDto loginDto = getGoodLoginDto();

        loginDto.setUserId(null);
        assertLogin(loginDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        loginDto.setUserId("");
        assertLogin(loginDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        loginDto.setUserId(" ");
        doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_ID)).when(userDetailsService).loadUserByUsername(loginDto.getUserId());
        assertLogin(loginDto, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_ID);
    }

    @Test
    public void validatePassword() throws Exception {

        LoginDto loginDto = getGoodLoginDto();

        loginDto.setPassword(null);
        assertLogin(loginDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        loginDto.setPassword("");
        assertLogin(loginDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        loginDto.setPassword(" ");
        doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_ID)).when(userDetailsService).loadUserByUsername(loginDto.getUserId());
        assertLogin(loginDto, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_ID);
    }

    private LoginDto getGoodLoginDto() {
        return LoginDto.builder()
                .userId("simulator")
                .password("de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0")
                .build();
    }

    private void assertLogin(LoginDto loginDto,
                             HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v1/login";

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
                        assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                    }
                });
    }

    private UserDetails getUserDetails(String userId, String password, AuthorityName authority) {
        Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(authority.toString()));
        UserDetails userDetails = new CustomUser(userId,
                passwordEncoder.encode(password),
                "username",
                "email",
                "phone",
                grantedAuthorities);
        return userDetails;
    }
}