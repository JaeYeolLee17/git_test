package com.e4motion.challenge.data.collector.dto;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.collector.HBaseMockBaseTest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@AutoConfigureMockMvc
class CameraLoginDtoTest extends HBaseMockBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    UserDetailsService userDetailsService;

    @Test
    public void validateOk() throws Exception {

        CameraLoginDto loginDto = getGoodLoginDto();

        doReturn(getUserDetails(loginDto.getCameraId(), loginDto.getPassword())).when(userDetailsService).loadUserByUsername(loginDto.getCameraId());

        assertLogin(loginDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    public void validateCameraId() throws Exception {

        CameraLoginDto loginDto = getGoodLoginDto();

        loginDto.setCameraId(null);
        assertLogin(loginDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        loginDto.setCameraId("");
        assertLogin(loginDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        loginDto.setCameraId(" ");
        doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_ID)).when(userDetailsService).loadUserByUsername(loginDto.getCameraId());
        assertLogin(loginDto, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_ID);
    }

    @Test
    public void validatePassword() throws Exception {

        CameraLoginDto loginDto = getGoodLoginDto();

        loginDto.setPassword(null);
        assertLogin(loginDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        loginDto.setPassword("");
        assertLogin(loginDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        loginDto.setPassword(" ");
        doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_ID)).when(userDetailsService).loadUserByUsername(loginDto.getCameraId());
        assertLogin(loginDto, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_ID);
    }

    private CameraLoginDto getGoodLoginDto() {
        return CameraLoginDto.builder()
                .cameraId("C0012")
                .password("de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0")
                .build();
    }

    private void assertLogin(CameraLoginDto loginDto,
                             HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v1/camera/login";

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

    private UserDetails getUserDetails(String cameraId, String password) {
        Set<GrantedAuthority> grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(AuthorityName.ROLE_CAMERA.toString()));
        UserDetails userDetails = new CustomUser(cameraId,
                passwordEncoder.encode(password),
                false,
                grantedAuthorities);
        return userDetails;
    }
}