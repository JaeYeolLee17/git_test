package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.service.CameraService;
import com.e4motion.challenge.data.collector.service.DataService;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.*;
import com.e4motion.common.utils.JsonHelper;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class DataControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataService dataService;

    @MockBean
    CameraService cameraService;

    @Test
    public void insertWithoutRole() throws Exception {
        assertInsert(getDataDto(), false,
                HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void insertWithAdminRole() throws Exception {
        assertInsert(getDataDto(), false,
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void insertWithManagerRole() throws Exception {
        assertInsert(getDataDto(), false,
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void insertWithUserRole() throws Exception {
        assertInsert(getDataDto(), false,
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "DATA")
    public void insertWithDataRole() throws Exception {
        assertInsert(getDataDto(), false,
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void insertWithCameraRole() throws Exception {

        CameraDataDto dataDto = getDataDto();

        doNothing().when(dataService).insert(dataDto);
        doReturn(true).when(cameraService).getSettingsUpdated(dataDto.getC());

        assertInsert(dataDto, true, HttpStatus.OK, Response.OK, null, null);

        doReturn(false).when(cameraService).getSettingsUpdated(dataDto.getC());
        assertInsert(dataDto, false, HttpStatus.OK, Response.OK, null, null);
    }

//    @Test
//    @WithMockUser(roles = "CAMERA")
//    public void insertWithCameraRoleWithInvalidData() throws Exception {
//
//        CameraDataDto dataDto = getDataDto();
//        dataDto.setTd(null);    // invalid traffic data
//
//        doThrow(new InvalidParamException(InvalidParamException.INVALID_DATA)).when(dataService).insert(dataDto);
//
//        assertInsert(dataDto, false, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
//    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void insertWithCameraRoleWithNonexistentCamera() throws Exception {

        CameraDataDto dataDto = getDataDto();

        doNothing().when(dataService).insert(dataDto);

        // Nonexistent camera id
        doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_ID)).when(cameraService).getSettingsUpdated(dataDto.getC());

        assertInsert(dataDto, false, HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE,CameraNotFoundException.INVALID_CAMERA_ID);
    }

    private CameraDataDto getDataDto() {
        return CameraDataDto.builder()
                .v("1.0")
                .c("C0001")
                .i("I0001")
                .r("R01")
                .t("")
                .td(new ArrayList<>())
                .build();
    }

    private void assertInsert(CameraDataDto dataDto, boolean expectedSettingsUpdated,
                              HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v1/data";

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
                        assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                    }
                });
    }
}