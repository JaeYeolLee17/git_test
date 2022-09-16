package com.e4motion.challenge.data.provider.controller;

import com.e4motion.challenge.common.constant.FilterBy;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.RegExpressions;
import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;

@Tag(name = "2. 교통 데이터")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "v2")
public class DataController {

    private static final String startTimePattern = RegExpressions.dateTime + "( [A-Z]\\d{4} [A-Z]\\d{4} [A-Z]\\d{2})?";

    private final static String TEXT_CSV = "text/csv";

    private final static int MAX_LIMIT = 100000;

    private final DataService dataService;

    @Operation(summary = "데이터", description = "접근 권한 : 데이터 사용자, MAX LIMIT : " + MAX_LIMIT)
    @PreAuthorize("hasRole('ROLE_DATA')")
    @GetMapping("/data")
    public Response get(@RequestParam(value = "startTime", required = true) @Pattern(regexp = startTimePattern) String startTime,
                        @RequestParam(value = "endTime", required = false) @Pattern(regexp = RegExpressions.dateTime) String endTime,
                        @RequestParam(value = "limit", required = true) @Min(1) @Max(MAX_LIMIT) Integer limit,
                        @RequestParam(value = "filterBy", required = false) FilterBy filterBy,
                        @RequestParam(value = "filterValue", required = false) String filterValue) {

        DataListDto dataList = dataService.get(makeRequestMap(startTime, endTime, limit, filterBy, filterValue));

        Response response = new Response("data", dataList.getData());
        response.setData("nextTime", dataList.getNextTime());

        return response;
    }

    @Operation(summary = "데이터 파일", description = "접근 권한 : 데이터 사용자, MAX LIMIT : " + MAX_LIMIT)
    @PreAuthorize("hasRole('ROLE_DATA')")
    @GetMapping(value = "/data/file", produces = TEXT_CSV)
    public void getAsFile(@RequestParam(value = "startTime", required = true) @Pattern(regexp = startTimePattern) String startTime,
                          @RequestParam(value = "endTime", required = false) @Pattern(regexp = RegExpressions.dateTime) String endTime,
                          @RequestParam(value = "limit", required = true) @Min(1) @Max(MAX_LIMIT) Integer limit,
                          @RequestParam(value = "filterBy", required = false) FilterBy filterBy,
                          @RequestParam(value = "filterValue", required = false) String filterValue,
                          HttpServletResponse response) throws IOException {

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(TEXT_CSV + "; charset=" + StandardCharsets.UTF_8.name());

        String filename = "data-" + LocalDateTime.now() + ".csv";
        response.setHeader("Content-disposition", "attachment;filename=" + filename);

        dataService.write(makeRequestMap(startTime, endTime, limit, filterBy, filterValue), response.getWriter());
    }

    private HashMap<String, Object> makeRequestMap(String startTime, String endTime, Integer limit, FilterBy filterBy, String filterValue) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("startTime", startTime);
        if (endTime != null) {
            map.put("endTime", endTime);
        }
        map.put("limit", limit);
        if (filterBy != null) {
            map.put("filterBy", filterBy);
            map.put("filterValue", filterValue);
        }
        return map;
    }
}
