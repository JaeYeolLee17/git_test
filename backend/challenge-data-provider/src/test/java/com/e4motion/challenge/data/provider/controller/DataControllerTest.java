package com.e4motion.challenge.data.provider.controller;

import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import com.e4motion.challenge.data.provider.dto.DataDto;
import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.service.DataService;
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
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class DataControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataService dataService;

    @Test
    public void queryWithoutRole() throws Exception {
        assertQuery(getGoodHashMap(),
                HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void queryWithAdminRole() throws Exception {
        assertQuery(getGoodHashMap(),
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "CAMERA")
    public void queryWithCameraRole() throws Exception {
        assertQuery(getGoodHashMap(),
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "DATA")
    public void queryWithDataRole() throws Exception {

        HashMap<String, Object> map = getGoodHashMap();

        doReturn(getDataListDto()).when(dataService).query(map);

        assertQuery(map, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "DATA")
    public void validateQueryParam() throws Exception {

        doReturn(getDataListDto()).when(dataService).query(any());

        // startTime
        HashMap<String, Object> map = getGoodHashMap();
        map.remove("startTime");                // missing
        assertQuery(map,
                HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        map.put("startTime", "2022-4-1 12:00:00");  // mis-format
        assertQuery(map,
                HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        // endTime
        map = getGoodHashMap();
        map.remove("endTime");                  // missing
        assertQuery(map, HttpStatus.OK, Response.OK, null, null);

        map.put("endTime", "2022-04-01");           // mis-format
        assertQuery(map,
                HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        // limit
        map = getGoodHashMap();
        map.remove("limit");        // missing
        assertQuery(map,
                HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        map.put("limit", "a");          // invalid type
        assertQuery(map,
                HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        map.put("limit", 0);            // out of range
        assertQuery(map,
                HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        map.replace("limit", 100001);   // out of range
        assertQuery(map,
                HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, InvalidParamException.INVALID_DATA);

        // filterBy, filterId
        map = getGoodHashMap();
        map.remove("filterBy");     // missing
        map.remove("filterId");
        assertQuery(map, HttpStatus.OK, Response.OK, null, null);
    }

    private DataListDto getDataListDto() {
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

        TrafficDataDto td = TrafficDataDto.builder()
                .st("2022-04-01 11:59:00")
                .et("2022-04-01 12:00:00")
                .p(1)
                .u(new Integer[5])
                .ld(ld)
                .build();

        List<DataDto> dataDto = new ArrayList<>();
        dataDto.add(DataDto.builder()
                .v("1.0")
                .c("C0001")
                .i("I0001")
                .r("R01")
                .t("2022-04-01 12:00:00")
                .td(td)
                .build());
        dataDto.add(DataDto.builder()
                .v("1.0")
                .c("C0001")
                .i("I0001")
                .r("R01")
                .t("2022-04-01 12:01:00")
                .td(td)
                .build());

        return DataListDto.builder()
                .nextTime("2022-04-01 12:00:00 C0001 I0001 R01")
                .data(dataDto)
                .build();
    }

    private HashMap getGoodHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("startTime", "2022-04-01 12:00:00");
        map.put("endTime", "2022-04-01 12:01:00");
        map.put("limit", 1);
        map.put("filterBy", "camera");
        map.put("filterId", "C0001");
        return map;
    }

    private void assertQuery(HashMap map,
                             HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v1/data?";
        if (map.get("startTime") != null) {
            uri += ("startTime=" + map.get("startTime"));
        }
        if (map.get("endTime") != null) {
            uri += ("&endTime=" + map.get("endTime"));
        }
        if (map.get("limit") != null) {
            uri += ("&limit=" + map.get("limit"));
        }
        if (map.get("filterBy") != null) {
            uri += ("&filterBy=" + map.get("filterBy") + "&filterId=" + map.get("filterId"));
        }

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.get("data")).isNotNull();
                        assertThat(body.get("nextTime")).isNotNull();
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                    }
                });
    }
}