package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

@Tag(name = "10. 날씨 API")
@RestController
@RequestMapping(path = "v2")
public class WeatherController {

    private final String location = "?q=";
    private final String appId = "&appid=";

    @Value("${openweather.api-key}")
    String apiKey;
    @Value("${openweather.base-url}")
    String baseUrl;

    @Operation(summary = "날씨 API 데이터", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/weather")
    // TODO : get method needs @RequestParam String location
    public Response get() throws Exception {

        String location = "Daegu";

        URI uri = getUri(location);

        RestTemplate template = new RestTemplate();
        ConcurrentHashMap weather = template.getForObject(uri, ConcurrentHashMap.class);

        return new Response("weather", weather);

    }

    private URI getUri(String selectedLocation) throws Exception {

        String url = baseUrl + location + Location.valueOf(selectedLocation) + appId + apiKey;

        return new URI(url);
    }

    public enum Location {
        Daegu
    }
}
