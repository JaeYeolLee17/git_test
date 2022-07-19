package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.collector.HBaseMockTest;
import com.e4motion.challenge.data.collector.dto.CameraLoginDto;
import com.e4motion.challenge.data.collector.service.CameraService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CameraControllerTest extends HBaseMockTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CameraService cameraService;

    @MockBean
    RestTemplate restTemplate;

    @Test
    public void getWithoutRole() throws Exception {

        assertGet("C0001", getGoodApiResponse(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
    }

    @Test
    @WithMockUser(username = "C0001", roles = "CAMERA")
    public void getWithCameraRole() throws Exception {

        String cameraNo = "C0001";

        assertThat(cameraService.getSettingsUpdated(cameraNo)).isTrue();

        assertGet("C0001", getGoodApiResponse(), HttpStatus.OK, Response.OK, null, null);

        assertThat(cameraService.getSettingsUpdated(cameraNo)).isFalse();

        // with bad response from api
        assertGet("C0001", null, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_NO);
        assertGet("C0001", null, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_NO);

        // with other camera
        assertGet("C0002", getGoodApiResponse(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(username = "C0099", roles = "CAMERA")
    public void getWithCameraRoleWithNonexistentCamera() throws Exception {

        // Nonexistent camera no
        assertGet("C0099", getGoodApiResponse(), HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_NO);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER", "DATA", "CAMERA_ADMIN"})
    public void getWithInaccessibleRole() throws Exception {

        assertGet("C0001", getGoodApiResponse(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    private Response getGoodApiResponse() {
        return new Response("camera", CameraLoginDto.builder().build());
    }

    private void assertGet(String cameraNo, Response expectedApiResponse,
                           HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        ResponseEntity<Response> entity = new ResponseEntity<>(expectedApiResponse, HttpStatus.OK);
        doReturn(entity).when(restTemplate).exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any());

        String uri = "/v2/camera/" + cameraNo;

        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.get("camera")).isNotNull();
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }

}