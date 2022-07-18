package com.e4motion.challenge.data.provider.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@AutoConfigureMockMvc
class DataControllerTest extends HBaseMockTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataService dataService;

    @Test
    public void queryWithoutRole() throws Exception {

        assertQuery(TestDataHelper.getQueryHashMap(),
                HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER", "CAMERA_ADMIN", "CAMERA"})
    public void queryWithInaccessibleRole() throws Exception {

        assertQuery(TestDataHelper.getQueryHashMap(),
                HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(roles = "DATA")
    public void queryWithDataRole() throws Exception {

        assertQuery(TestDataHelper.getQueryHashMap(), HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "DATA")
    public void validateQueryParam() throws Exception {

        // startTime
        HashMap<String, Object> map = TestDataHelper.getQueryHashMap();
        map.remove("startTime");                // missing
        assertQuery(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.put("startTime", "2022-4-1 12:00:00");  // mis-format
        assertQuery(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // endTime
        map = TestDataHelper.getQueryHashMap();
        map.remove("endTime");                  // missing
        assertQuery(map, HttpStatus.OK, Response.OK, null, null);

        map.put("endTime", "2022-04-01");           // mis-format
        assertQuery(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // limit
        map = TestDataHelper.getQueryHashMap();
        map.remove("limit");        // missing
        assertQuery(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.put("limit", "a");          // invalid type
        assertQuery(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.put("limit", 0);            // out of range
        assertQuery(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        map.replace("limit", 100001);   // out of range
        assertQuery(map, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // filterBy, filterId
        map = TestDataHelper.getQueryHashMap();
        map.remove("filterBy");     // missing
        map.remove("filterId");
        assertQuery(map, HttpStatus.OK, Response.OK, null, null);
    }

    private void assertQuery(HashMap<String, Object> map,
                             HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        doReturn(TestDataHelper.getDataListDto()).when(dataService).query(any());

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
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }
}