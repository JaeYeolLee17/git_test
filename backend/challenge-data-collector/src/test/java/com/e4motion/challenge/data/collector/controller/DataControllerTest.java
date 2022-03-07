package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.service.CameraService;
import com.e4motion.challenge.data.collector.service.DataService;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
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
import java.util.List;

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
        List<LaneDataDto> ld = new ArrayList<>();
        ld.add(LaneDataDto.builder()
                .ln(1)
                .qml(5)
                .qm(new Integer[5])
                .qal(6.3f)
                .qa(new Float[5] )
                .s(new Integer[5])
                .l(new Integer[5])
                .r(new Integer[5])
                .build());

        List<TrafficDataDto> td = new ArrayList<>();
        td.add(TrafficDataDto.builder()
                .st("2022-04-01 11:59:00")
                .et("2022-04-01 12:00:00")
                .p(1)
                .u(new Integer[5])
                .ld(ld)
                .build());

        return CameraDataDto.builder()
                .v("1.0")
                .c("C0001")
                .i("I0001")
                .r("R01")
                .t("2022-04-01 12:00:00")
                .td(td)
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