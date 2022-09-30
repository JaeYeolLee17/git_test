package com.e4motion.challenge.data.provider.controller;

import com.e4motion.challenge.common.constant.FilterBy;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.e4motion.challenge.data.provider.HBaseMockTest;
import com.e4motion.challenge.data.provider.TestDataHelper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DataControllerTest extends HBaseMockTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataService dataService;

    @Test
    public void getWithoutRole() throws Exception {

        assertGet(getGoodGetParams(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER", "CAMERA_ADMIN", "CAMERA"})
    public void getWithInaccessibleRole() throws Exception {

        assertGet(getGoodGetParams(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "DATA")
    public void getWithDataRole() throws Exception {

        assertGet(getGoodGetParams(), HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "DATA")
    public void validateGetParams() throws Exception {

        // startTime
        HashMap<String, Object> map = getGoodGetParams();
        map.remove("startTime");                // missing
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.put("startTime", "2022-4-1 12:00:00");
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("startTime", " 2022-04-01 12:00:00");
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("startTime", "2022-04-01 12:00:00 ");
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("startTime", "2021-10-14 01:44:10 C0001 ");
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("startTime", "2021-10-14 01:44:10 C0001 I0001 R01");
        assertGet(map, HttpStatus.OK, Response.OK, null, null);

        map.replace("startTime", "2022-04-01 12:00:00");
        assertGet(map, HttpStatus.OK, Response.OK, null, null);

        // endTime
        map = getGoodGetParams();
        map.remove("endTime");                  // missing
        assertGet(map, HttpStatus.OK, Response.OK, null, null);

        map.put("endTime", "2022-04-01");
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("endTime", " 2022-04-01 12:00:00");
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("endTime", "2022-04-01 12:00:00 ");
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("endTime", "2021-10-14 01:44:10 C0001 I0001 R01");
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("endTime", "2022-04-01 12:00:00");
        assertGet(map, HttpStatus.OK, Response.OK, null, null);

        // limit
        map = getGoodGetParams();
        map.remove("limit");        // missing
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.put("limit", "a");          // invalid type
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.put("limit", 0);            // out of range
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("limit", 100001);   // out of range
        assertGet(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // filterBy, filterValue
        map = getGoodGetParams();
        map.remove("filterBy");     // missing
        map.remove("filterValue");
        assertGet(map, HttpStatus.OK, Response.OK, null, null);
    }

    public static HashMap<String, Object> getGoodGetParams() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("startTime", "2022-04-01 12:00:00");
        map.put("endTime", "2022-04-01 12:01:00");
        map.put("limit", 1);
        map.put("filterBy", FilterBy.CAMERA);
        map.put("filterValue", "C0001");
        return map;
    }

    private void assertGet(HashMap<String, Object> map,
                           HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        doReturn(TestDataHelper.getDataListDto()).when(dataService).get(any());

        String uri = "/v2/data?";

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
            uri += ("&filterBy=" + map.get("filterBy") + "&filterValue=" + map.get("filterValue"));
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
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }
}