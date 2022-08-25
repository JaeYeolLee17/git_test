package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.response.ResponseFail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

@Tag(name = "10. 날씨 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class WeatherController {

    private static final String LOCATION = "?q=";
    private static final String APP_ID = "&appid=";

    private final RestTemplate restTemplate;

    @Value("${openweather.url}")
    String url;
    @Value("${openweather.api-key}")
    String apiKey;

    @Operation(summary = "날씨 API 데이터", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/weather")
    // TODO : get method needs @RequestParam String location
    public Response get() throws Exception {

        Location location = Location.valueOf("Daegu");

        URI uri = getUri(location);
        log.debug("uri: " + uri);

        ConcurrentHashMap weather = null;
        try {
            ResponseEntity<ConcurrentHashMap> entity = restTemplate.exchange(uri,
                    HttpMethod.GET, null, ConcurrentHashMap.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                weather = entity.getBody();
                log.debug("weather: " + weather);
                return new Response("weather", weather);
            }
        } catch (Exception e) {
            log.info("exception: " + e.toString());
        }

        return new ResponseFail("OPEN_WEATHER_ERROR", "");
    }

    private URI getUri(Location location) throws Exception {

        return new URI(url + LOCATION + location.name() + APP_ID + apiKey);
    }

    public enum Location {
        Daegu
    }
}
