package com.e4motion.challenge.api.service;


import com.e4motion.challenge.api.controller.OpenWeatherController;
import com.e4motion.challenge.common.exception.customexception.OpenWeatherException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class OpenWeatherServiceTest {

    private static String LOCATION;
    private static String APP_ID;

    @Value("${openweather.url}")
    public String testBaseUrl;
    @Value("${openweather.api-key}")
    public String testApiKey;

    @Autowired
    private OpenWeatherService openWeatherService;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    void weatherServiceIsExist() {
        assertThat(openWeatherService).isNotNull();
    }

    @BeforeEach
    void testConst() {

        try {
            Field field1 = openWeatherService.getClass().getDeclaredField("LOCATION");
            field1.setAccessible(true);
            LOCATION= field1.get(openWeatherService).toString();

            Field field2 = openWeatherService.getClass().getDeclaredField("APP_ID");
            field2.setAccessible(true);
            APP_ID = field2.get(openWeatherService).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void weatherUrlTest() {

        assertThat(testBaseUrl).isNotNull();
        assertThat(testApiKey).isNotNull();
        assertThat(testBaseUrl).isEqualTo("https://api.openweathermap.org/data/2.5/weather");
        assertThat(testApiKey).isEqualTo("107ae00c8ec9e7f2cb859c5e00342bc4");
        assertThat(OpenWeatherController.Location.Daegu).isEqualTo(OpenWeatherController.Location.valueOf("Daegu"));

        assertThat(LOCATION).isEqualTo("?q=");
        assertThat(APP_ID).isEqualTo("&appid=");

        String testUrl = testBaseUrl + LOCATION + "Daegu" + APP_ID + testApiKey;
        String url = testBaseUrl + LOCATION + OpenWeatherController.Location.valueOf("Daegu") + APP_ID + testApiKey;
        assertThat(url).isEqualTo(testUrl);
    }

    @ParameterizedTest
    @EnumSource(OpenWeatherController.Location.class)
    void testGet() {

        try {

            String testUrl = "https://api.openweathermap.org/data/2.5/weather?q=Daegu&appid=107ae00c8ec9e7f2cb859c5e00342bc4";
            String testUrl2 = testBaseUrl + LOCATION + OpenWeatherController.Location.Daegu + APP_ID + testApiKey;

            ConcurrentHashMap testResult = restTemplate.exchange("https://api.openweathermap.org/data/2.5/weather?q=Daegu&appid=107ae00c8ec9e7f2cb859c5e00342bc4", HttpMethod.GET, null, ConcurrentHashMap.class).getBody();
            ConcurrentHashMap testResult2 = restTemplate.exchange(testUrl2, HttpMethod.GET, null, ConcurrentHashMap.class).getBody();

            assertThat(testResult).isNotNull();
            assertThat(testResult2).isNotNull();
            assertThat(testResult).isEqualTo(testResult2);

            Method method = openWeatherService.getClass().getDeclaredMethod("get", OpenWeatherController.Location.class);
            method.setAccessible(true);
            method.getParameters();

            ConcurrentHashMap result = (ConcurrentHashMap) method.invoke(openWeatherService, OpenWeatherController.Location.Daegu);

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(testResult);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    void weatherErrorTest() throws Exception {

        ReflectionTestUtils.setField(openWeatherService, "apiKey", "");
        Assertions.assertThrows(OpenWeatherException.class, () -> openWeatherService.get(OpenWeatherController.Location.Daegu));

        ReflectionTestUtils.setField(openWeatherService, "baseUrl", "");
        Assertions.assertThrows(OpenWeatherException.class, () -> openWeatherService.get(OpenWeatherController.Location.Daegu)).getCode();

    }

}