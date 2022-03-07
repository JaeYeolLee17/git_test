package com.e4motion.challenge.data.collector.dto;

import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.collector.service.CameraService;
import com.e4motion.challenge.data.collector.service.DataService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@AutoConfigureMockMvc
class CameraDataDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataService dataService;

    @MockBean
    CameraService cameraService;

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateOk() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();

        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateV_C() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();

        dataDto.setV(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setV("");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setV(" ");
        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);

        dataDto.setC(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setC("");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setC(" ");
        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateT() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();

        dataDto.setT(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setT("2022-04-01");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setT("2022-04-01 12:00");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setT("22-04-01 12:00:00");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setT("2022-04-01 12:00:00.500");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();

        dataDto.getTd().clear();    // size 0
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        dataDto.setTd(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_St_Et() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);

        // St
        td.setSt(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setSt("");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setSt(" ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setSt("2022-04-01");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setSt("2022-4-1 12:00:00");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setSt("2022-04-01 12:00:00 ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setSt("2022-04-01 12:00:00");
        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);

        // Et
        td.setEt(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setEt("");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setEt(" ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setEt("2022-04-01");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setEt("2022-4-1 12:00:00");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setEt("2022-04-01 12:00:00 ");
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_U() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);

        td.setU(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setU(new Integer[4]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setU(new Integer[6]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_Ld() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);

        td.getLd().clear();     // size 0
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        td.setLd(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_Ld_Ln() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);
        LaneDataDto ld = td.getLd().get(0);

        ld.setLn(0);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setLn(11);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setLn(10);
        assertInsert(dataDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void validateTd_Ld_Qm_Qa_S_L_R() throws Exception {

        CameraDataDto dataDto = getGoodDataDto();
        TrafficDataDto td = dataDto.getTd().get(0);
        LaneDataDto ld = td.getLd().get(0);

        // qm
        ld.setQm(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setQm(new Integer[4]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setQm(new Integer[6]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        // qa
        ld.setQa(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setQa(new Float[1]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setQa(new Float[10]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        // s
        ld.setS(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setS(new Integer[3]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setS(new Integer[7]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        // l
        ld.setL(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setL(new Integer[2]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setL(new Integer[100]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        // r
        ld.setR(null);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setR(new Integer[0]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        ld.setR(new Integer[9]);
        assertInsert(dataDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);
    }

    private CameraDataDto getGoodDataDto() {
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

    private void assertInsert(CameraDataDto dataDto,
                              HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v1/data";

        doNothing().when(dataService).insert(dataDto);
        doReturn(true).when(cameraService).getSettingsUpdated(dataDto.getC());

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(dataDto)))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        //
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                    }
                });
    }
}