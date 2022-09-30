package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.constant.WeatherArea;
import com.e4motion.challenge.api.service.WeatherService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "11. 날씨")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(summary = "날씨 정보", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/weather")
    public Response get(@RequestParam(required = true) WeatherArea area) throws Exception {

        return new Response("weather", weatherService.get(area));
    }
}

