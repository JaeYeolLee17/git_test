package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.constant.WeatherArea;
import com.e4motion.challenge.api.dto.WeatherDto;
import com.e4motion.challenge.api.service.WeatherService;
import com.e4motion.challenge.common.exception.customexception.OpenWeatherException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class OpenWeatherServiceImpl implements WeatherService {

    private final static String LOCATION = "?q=";
    private final static String APP_ID = "&appid=";

    private final RestTemplate restTemplate;

    @Value("${openweather.url}")
    public String baseUrl;
    @Value("${openweather.api-key}")
    public String apiKey;

    public WeatherDto get(WeatherArea area) {

        try {
            return restTemplate.getForObject(getUrl(area), WeatherDto.class, area);
        }
        catch (RestClientException e) {
            throw new OpenWeatherException(e.getMessage());
        }
    }

    private String getUrl(WeatherArea area) {
        return baseUrl +
                LOCATION +
                area +
                APP_ID +
                apiKey;
    }
}

