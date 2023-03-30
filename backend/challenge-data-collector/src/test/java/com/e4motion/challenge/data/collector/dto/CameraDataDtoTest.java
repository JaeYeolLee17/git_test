package com.e4motion.challenge.data.collector.dto;

import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.security.SecurityHelper;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.collector.HBaseMockTest;
import com.e4motion.challenge.data.collector.TestDataHelper;
import com.e4motion.challenge.data.collector.service.CameraService;
import com.e4motion.challenge.data.collector.service.DataService;
import com.e4motion.challenge.data.collector.service.DataStatsService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CameraDataDtoTest extends HBaseMockTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataService dataService;

    @MockBean
    DataStatsService dataStatsService;

    @MockBean
    CameraService cameraService;

    @MockBean
    SecurityHelper securityHelper;

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateOk() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();

        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateV_C() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();

        dataDto.setV(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setV("");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setV(" ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setV("version");
        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);

        dataDto.setC(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setC("");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setC(" ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateT() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();

        dataDto.setT(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setT("2022-04-01");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setT("2022-04-01 12:00");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setT("22-04-01 12:00:00");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setT("2022-04-01 12:00:00.500");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();

        dataDto.getTd().clear();    // size 0
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        dataDto.setTd(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_St_Et() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);

        // St
        td.setSt(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setSt("");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setSt(" ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setSt("2022-04-01");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setSt("2022-4-1 12:00:00");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setSt("2022-04-01 12:00:00 ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setSt("2022-04-01 12:00:00");
        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);

        // Et
        td.setEt(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setEt("");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setEt(" ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setEt("2022-04-01");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setEt("2022-4-1 12:00:00");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setEt("2022-04-01 12:00:00 ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_U() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);

        td.setU(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setU(new Integer[4]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setU(new Integer[7]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_Ld() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);

        td.getLd().clear();     // size 0
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        td.setLd(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_Ld_Ln() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);
        LaneDataDto ld = td.getLd().get(0);

        ld.setLn(0);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setLn(13);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setLn(12);
        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_Ld_Qm_Qa_S_L_R() throws Exception {

        CameraDataDto dataDto = TestDataHelper.getCameraDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);
        LaneDataDto ld = td.getLd().get(0);

        // qm
        ld.setQm(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setQm(new Integer[4]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setQm(new Integer[7]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // qa
        ld.setQa(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setQa(new Float[1]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setQa(new Float[10]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // s
        ld.setS(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setS(new Integer[3]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setS(new Integer[7]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // l
        ld.setL(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setL(new Integer[2]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setL(new Integer[100]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // r
        ld.setR(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setR(new Integer[0]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        ld.setR(new Integer[9]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    private void assertInsert(CameraDataDto dataDto,
                              HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/data";

        doReturn(true).when(dataService).insert(dataDto);
        doReturn(1L).when(dataStatsService).insert(dataDto);
        doReturn(true).when(cameraService).getSettingsUpdated(dataDto.getC());
        doNothing().when(securityHelper).checkIfLoginCameraForRoleCamera(dataDto.getC());

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(dataDto)))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.get("settingsUpdated")).isEqualTo(true);
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }
}