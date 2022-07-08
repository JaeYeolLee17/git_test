package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.data.collector.HBaseMockTest;
import com.e4motion.challenge.data.collector.TestDataHelper;
import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.service.CameraService;
import com.e4motion.challenge.data.collector.service.DataService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class DataControllerTest extends HBaseMockTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataService dataService;

    @MockBean
    CameraService cameraService;

    @Test
    public void insertWithoutRole() throws Exception {

        assertInsert(TestDataHelper.getDataDto(), false,
                HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void insertWithAdminRole() throws Exception {

        assertInsert(TestDataHelper.getDataDto(), false,
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void insertWithManagerRole() throws Exception {

        assertInsert(TestDataHelper.getDataDto(), false,
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void insertWithUserRole() throws Exception {

        assertInsert(TestDataHelper.getDataDto(), false,
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "DATA")
    public void insertWithDataRole() throws Exception {

        assertInsert(TestDataHelper.getDataDto(), false,
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void insertWithCameraRole() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getDataDto();

        doNothing().when(dataService).insert(dataDto);
        doReturn(true).when(cameraService).getSettingsUpdated(dataDto.getC());

        assertInsert(dataDto, true, HttpStatus.OK, Response.OK, null, null);

        doReturn(false).when(cameraService).getSettingsUpdated(dataDto.getC());
        assertInsert(dataDto, false, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void insertWithCameraRoleWithNonexistentCamera() throws Exception {

        // TODO: insert 시 로그인 카메라가 자기 자신인지 체크한다면 camera not found 뜨기 전에 권한 없음이 뜰 것이다. 처리!!!
        CameraDataDto dataDto = TestDataHelper.getDataDto();

        doNothing().when(dataService).insert(dataDto);

        // Nonexistent camera id
        doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO)).when(cameraService).getSettingsUpdated(dataDto.getC());

        assertInsert(dataDto, false, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE,CameraNotFoundException.INVALID_CAMERA_NO);
    }

    private void assertInsert(CameraDataDto dataDto, boolean expectedSettingsUpdated,
                              HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/data";

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(dataDto)))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.get("settingsUpdated")).isEqualTo(expectedSettingsUpdated);
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }

}