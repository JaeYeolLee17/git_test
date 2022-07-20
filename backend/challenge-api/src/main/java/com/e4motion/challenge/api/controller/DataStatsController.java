package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.common.domain.FilterBy;
import com.e4motion.challenge.common.domain.GroupBy;
import com.e4motion.challenge.api.dto.StatsMfdDto;
import com.e4motion.challenge.api.service.DataStatsService;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.DateTimeHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "7. 데이터 통계")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class DataStatsController {

    private final DataStatsService dataStatsService;

    @Operation(summary = "교통통계: 링크(15분 단위)", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
	@GetMapping("/data/stats/link")
    public Response getLink(@RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime startTime,
                            @RequestParam(value = "endTime", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime endTime,
                            @RequestParam(value = "filterBy", required = false) FilterBy filterBy,
                            @RequestParam(value = "filterId", required = false) String filterId) {

        dataStatsService.getLink(startTime, endTime, filterBy, filterId);

        return new Response("stats", "ok");
    }

    @Operation(summary = "교통통계: 교통도(15분 단위)", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/data/stats/mfd")
    public Response getMfd(@RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime startTime,
                           @RequestParam(value = "endTime", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime endTime,
                           @RequestParam(value = "groupBy", required = false) GroupBy groupBy,
                           @RequestParam(value = "filterBy", required = false) FilterBy filterBy,
                           @RequestParam(value = "filterId", required = false) String filterId,
                           @RequestParam(value = "dayOfWeek", required = false) Integer dayOfWeek) {

        List<StatsMfdDto> statsMfdDtos = dataStatsService.getMfd(startTime, endTime, groupBy, filterBy, filterId, dayOfWeek);

        return new Response("stats", statsMfdDtos);
    }

    @Operation(summary = "교통통계: 기간별(1시간 단위)", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/data/stats/period")
    public Response getPeriod(@RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime startTime,
                              @RequestParam(value = "endTime", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime endTime,
                              @RequestParam(value = "byPeriod", required = true) String byPeriod,
                              @RequestParam(value = "groupBy", required = false) GroupBy groupBy,
                              @RequestParam(value = "filterBy", required = false) FilterBy filterBy,
                              @RequestParam(value = "filterId", required = false) String filterId) {

        dataStatsService.getPeriod(startTime, endTime, byPeriod, groupBy, filterBy, filterId);

        return new Response("stats", "ok");
    }

    @Operation(summary = "교통통계: 일일 상세(1시간 단위)", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/data/stats/daily")
    public Response getDaily(@RequestParam(value = "date", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateFormat) LocalDate date,
                             @RequestParam(value = "groupBy", required = false) GroupBy groupBy,
                             @RequestParam(value = "filterBy", required = false) FilterBy filterBy,
                             @RequestParam(value = "filterId", required = false) String filterId) {

        dataStatsService.getDaily(date, groupBy, filterBy, filterId);

        return new Response("stats", "ok");
    }
}
