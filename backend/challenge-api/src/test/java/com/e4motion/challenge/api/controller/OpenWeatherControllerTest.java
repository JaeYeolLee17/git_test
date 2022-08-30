package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.service.OpenWeatherService;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OpenWeatherControllerTest {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OpenWeatherService openWeatherService;

    private Gson gson;


    @Test
    public void mockMvcIsNotNull() throws Exception {
        assertThat(mockMvc).isNotNull();
    }


    private void isGetOk(HttpStatus expectedStatus, String expectedResult) throws Exception {

        String url = "/v2/weather";

        mockMvc
                .perform(MockMvcRequestBuilders.get(url))
                .andExpect(result -> {
                            MockHttpServletResponse response = result.getResponse();
                            assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                            Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                            assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                        });

    }

    private void isDataOk(String location, HttpStatus expectedStatus, String expectedResult) throws Exception {

        String url = "/v2/weather";

        ConcurrentHashMap expectedData =
                restTemplate.exchange("https://api.openweathermap.org/data/2.5/weather?q=Daegu&appid=107ae00c8ec9e7f2cb859c5e00342bc4",
                        HttpMethod.GET,
                        null,
                        ConcurrentHashMap.class).getBody();

        mockMvc
                .perform(MockMvcRequestBuilders.get(url).param("location", location))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);

                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.getData("weather")).isNotNull();
                        assertThat(body.getData("weather")).isEqualTo(expectedData);
                    } else {
                        assertThat(body.getData("weather")).isNull();
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getTest() throws Exception {

        isGetOk(HttpStatus.OK, Response.OK);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void dataTest() throws Exception {

        isDataOk("Daegu", HttpStatus.OK, Response.OK);
        isDataOk("asd", HttpStatus.BAD_REQUEST, Response.FAIL);
    }

}
