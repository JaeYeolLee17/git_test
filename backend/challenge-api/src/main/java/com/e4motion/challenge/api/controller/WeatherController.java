package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.WeatherArea;
import com.e4motion.challenge.api.service.OpenWeatherService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "10. 날씨")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class WeatherController {

    // TODO: by sjkim
    // TODO: Area enum 안에 정의된 string 이 여기 다시 string 으로 쓰여진 것이 부적절해보입니다.
    // TODO: defaultValue 에 꼭 string 만을 써야 한다면. 그냥 required=true여도 되지 않을까요? Area parameter 가 없는 경우 어떻게 되나요?
    private static final String AREA_DEFAULT = "Daegu";

    // TODO: weather service interface 이름은 그냥 WeatherService 로 합시다.
    // TODO: 그것의 구현체 이름이 OpenWeatherService 가 되겠지요.
    private final OpenWeatherService openWeatherService;

    @Operation(summary = "날씨 데이터", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/weather")
    public Response get(@RequestParam(required = false, defaultValue = AREA_DEFAULT) WeatherArea area) throws Exception {

        return new Response("weather", openWeatherService.get(area));
    }
}

