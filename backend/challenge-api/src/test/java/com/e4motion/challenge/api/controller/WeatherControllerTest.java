package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.Area;
import com.e4motion.challenge.api.dto.WeatherDto;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mockMvcIsNotNull() throws Exception {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void getWithoutRole() throws Exception {

        assertGet(HttpStatus.UNAUTHORIZED, Response.FAIL, Area.Daegu, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getWithAdminRole() throws Exception {

        WeatherDto expectedWeather = create();

        assertGet(HttpStatus.OK, Response.OK, Area.Daegu, expectedWeather);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void getWithManagerRole() throws Exception {

        assertGet(HttpStatus.OK, Response.OK, Area.Daegu, create());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getWithUserRole() throws Exception {

        assertGet(HttpStatus.OK, Response.OK, Area.Daegu, create());
    }

    @Test
    @WithMockUser(roles = {"CAMERA_ADMIN", "DATA"})
    public void getWithUnauthorizedRole() throws Exception {

        assertGet(HttpStatus.FORBIDDEN, Response.FAIL, Area.Daegu, null);
    }

    // TODO: by sjkim
    // TODO: 이름? 무엇을 create?
    private WeatherDto create() throws JsonProcessingException {

        String jsonData = "{\"coord\":{\"lon\":128.55,\"lat\":35.8},\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"base\":\"stations\",\"main\":{\"temp\":293.51,\"feels_like\":294.16,\"temp_min\":293.51,\"temp_max\":293.51,\"pressure\":1010,\"humidity\":98},\"visibility\":9742,\"wind\":{\"speed\":0.58,\"deg\":37},\"clouds\":{\"all\":100},\"dt\":1661843412,\"sys\":{\"type\":1,\"id\":5507,\"country\":\"KR\",\"sunrise\":1661806558,\"sunset\":1661853461},\"timezone\":32400,\"id\":1835327,\"name\":\"Daegu\",\"cod\":200}";

        return JsonHelper.fromJson(jsonData, WeatherDto.class);
    }

    private void assertGet(HttpStatus expectedStatus, String expectedResult, Area area, WeatherDto expectedData) throws Exception {

        String url = "/v2/weather";

        mockMvc.perform(MockMvcRequestBuilders.get(url).param("area", area.name()))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);

                    if (expectedResult.equals(Response.OK)) {

                        assertThat(body.getData("weather")).isNotNull();

                        LinkedHashMap weather = (LinkedHashMap) body.get("weather");

                        assertThat(weather.get("id")).isEqualTo(expectedData.getId());
                    } else {
                        assertThat(body.getData("weather")).isNull();
                    }
                });
    }

}
