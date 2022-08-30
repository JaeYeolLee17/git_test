package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.controller.OpenWeatherController;
import com.e4motion.challenge.api.service.OpenWeatherService;
import com.e4motion.challenge.common.exception.customexception.OpenWeatherException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

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

    public ConcurrentHashMap get(OpenWeatherController.Location location) throws Exception {

        try {

            return restTemplate.exchange(
                baseUrl +
                    LOCATION +
                    location +
                    APP_ID +
                    apiKey,
                    HttpMethod.GET, null, ConcurrentHashMap.class).getBody();
        }
        catch (Exception e) {
            throw new OpenWeatherException(e.getMessage());
        }

    }
}

