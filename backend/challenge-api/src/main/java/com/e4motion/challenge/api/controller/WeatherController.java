package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.Area;
import com.e4motion.challenge.api.service.OpenWeatherService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "10. 날씨 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class WeatherController {

    private final static String AREA_DEFAULT = "Daegu";
    private final OpenWeatherService openWeatherService;

    @Operation(summary = "날씨 API 데이터", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/weather")
    public Response get(@RequestParam(required = false, defaultValue = AREA_DEFAULT) Area area) throws Exception {

        return new Response("weather", openWeatherService.get(area));
    }
}

