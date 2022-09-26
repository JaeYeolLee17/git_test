package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.constant.WeatherArea;
import com.e4motion.challenge.api.dto.WeatherDto;
import com.e4motion.challenge.common.exception.customexception.OpenWeatherException;
import com.e4motion.challenge.common.utils.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
class WeatherServiceTest {

    @Autowired
    WeatherService weatherService;

    @MockBean
    RestTemplate restTemplate;

    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=Daegu&appid=107ae00c8ec9e7f2cb859c5e00342bc4";

    @Test
    public void get() throws Exception {

        WeatherArea area = WeatherArea.Daegu;

        WeatherDto expectedWeather = createWeatherDto(area);

        doReturn(expectedWeather).when(restTemplate).getForObject(url, WeatherDto.class, area);

        assertThat(weatherService.get(area)).isNotNull();
        assertThat(weatherService.get(area).getId()).isEqualTo(expectedWeather.getId());
    }

    @Test
    public void getError() {

        doThrow(RestClientException.class).when(restTemplate).getForObject(url, WeatherDto.class, WeatherArea.Daegu);

        Assertions.assertThrows(OpenWeatherException.class, () -> weatherService.get(WeatherArea.Daegu));
    }

    private WeatherDto createWeatherDto(WeatherArea area) throws JsonProcessingException {

        String jsonData = "{\"coord\":{\"lon\":128.55,\"lat\":35.8},\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"base\":\"stations\",\"main\":{\"temp\":293.51,\"feels_like\":294.16,\"temp_min\":293.51,\"temp_max\":293.51,\"pressure\":1010,\"humidity\":98},\"visibility\":9742,\"wind\":{\"speed\":0.58,\"deg\":37},\"clouds\":{\"all\":100},\"dt\":1661843412,\"sys\":{\"type\":1,\"id\":5507,\"country\":\"KR\",\"sunrise\":1661806558,\"sunset\":1661853461},\"timezone\":32400,\"id\":1835327,\"name\":\"Daegu\",\"cod\":200}";

        return JsonHelper.fromJson(jsonData, WeatherDto.class);
    }
}