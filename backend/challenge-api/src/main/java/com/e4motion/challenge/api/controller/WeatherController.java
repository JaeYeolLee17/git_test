package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

@Tag(name = "10. 날씨 API")
@Slf4j
@RestController
@RequestMapping(path = "v2")
public class WeatherController {

    private final static String LOCATION = "?q=";
    private final static String APP_ID = "&appid=";

    @Value("${openweather.url}")
    String url;
    @Value("${openweather.api-key}")
    String apiKey;

    @Operation(summary = "날씨 API 데이터", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/weather")
    // TODO : get method needs @RequestParam String location
    public Response get() throws Exception {

        String location = "Daegu";

        URI uri = getUri(location);
        log.debug("uri: " + uri);

        RestTemplate template = new RestTemplate();
        ConcurrentHashMap weather = template.getForObject(uri, ConcurrentHashMap.class);
        log.debug("weather: " + weather);

        return new Response("weather", weather);
    }

    private URI getUri(String selectedLocation) throws Exception {

        return new URI(url + LOCATION + Location.valueOf(selectedLocation) + APP_ID + apiKey);
    }

    public enum Location {
        Daegu
    }
}
