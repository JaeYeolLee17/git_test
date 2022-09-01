package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.dto.Area;
import com.e4motion.challenge.api.dto.WeatherDto;
import com.e4motion.challenge.api.service.OpenWeatherService;
import com.e4motion.challenge.common.exception.customexception.OpenWeatherException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class OpenWeatherServiceImpl implements OpenWeatherService {

    private final static String LOCATION = "?q=";
    private final static String APP_ID = "&appid=";

    private final RestTemplate restTemplate;

    @Value("${openweather.url}")
    public String baseUrl;
    @Value("${openweather.api-key}")
    public String apiKey;

    public WeatherDto get(Area area) {

        try {

            return restTemplate.getForObject(getUrl(area), WeatherDto.class, area);
        }
        catch (RestClientException e) {
            throw new OpenWeatherException(e.getMessage());
        }
    }

    private String getUrl(Area area) {
        return baseUrl +
                LOCATION +
                area +
                APP_ID +
                apiKey;
    }
}

