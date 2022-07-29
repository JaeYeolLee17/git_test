package com.e4motion.challenge.data.collector.dto;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.collector.HBaseMockTest;
import com.e4motion.challenge.data.collector.security.CustomUser;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CameraLoginDtoTest extends HBaseMockTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    UserDetailsService userDetailsService;

    @Test
    public void validateOk() throws Exception {

        String cameraNo = "C0012";
        String password = "camera12!@";

        doReturn(getUserDetails(cameraNo, password)).when(userDetailsService).loadUserByUsername(cameraNo);

        assertLogin(cameraNo, password, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    public void validateCameraNo() throws Exception {

        String cameraNo = null;
        String password = "camera12!@";
        assertLogin(cameraNo, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        cameraNo = "";
        assertLogin(cameraNo, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        cameraNo = " ";
        assertLogin(cameraNo, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        cameraNo = "not existent camera";
        doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO)).when(userDetailsService).loadUserByUsername(cameraNo);
        assertLogin(cameraNo, password, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_NO);
    }

    @Test
    public void validatePassword() throws Exception {

        String cameraNo = "C0012";
        String password = null;
        assertLogin(cameraNo, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        password = "";
        assertLogin(cameraNo, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        password = " ";
        assertLogin(cameraNo, password, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    private void assertLogin(String cameraNo, String password,
                             HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/camera/login";

        CameraLoginDto loginDto = CameraLoginDto.builder()
                .cameraNo(cameraNo)
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
                        assertThat(body.get("token")).isNotNull();
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }

    private UserDetails getUserDetails(String cameraNo, String password) {

        Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(AuthorityName.ROLE_CAMERA.toString()));

        return new CustomUser(cameraNo,
                passwordEncoder.encode(password),
                false,
                grantedAuthorities);
    }
}