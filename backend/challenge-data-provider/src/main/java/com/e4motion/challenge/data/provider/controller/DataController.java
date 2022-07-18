package com.e4motion.challenge.data.provider.controller;

import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Tag(name = "2. 교통 데이터")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "v2")
public class DataController {

    private final static int MAX_LIMIT = 100000;

    private final DataService dataService;

    @Operation(summary = "데이터", description = "접근 권한 : 데이터 사용자, 최대 한도 : 10만건")
    @PreAuthorize("hasRole('ROLE_DATA')")
    @GetMapping("/data")
    public Response get(@RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime startTime,
                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime endTime,
                        @RequestParam(value = "limit", required = true) @Min(1) @Max(MAX_LIMIT) Integer limit,
                        @RequestParam(value = "filterBy", required = false) String filterBy,
                        @RequestParam(value = "filterId", required = false) String filterId) {

        DataListDto dataList = dataService.get(getRequestMap(startTime, endTime, limit, filterBy, filterId));

        Response response = new Response("data", dataList.getData());
        response.setData("nextTime", dataList.getNextTime());

        return response;
    }

    @Operation(summary = "데이터 파일", description = "접근 권한 : 데이터 사용자, 최대 한도 : 10만건")
    @PreAuthorize("hasRole('ROLE_DATA')")
    @GetMapping(value = "/data/file", produces = "text/csv")
    public void getAsFile(@RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime startTime,
                          @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = DateTimeHelper.dateTimeFormat) LocalDateTime endTime,
                          @RequestParam(value = "limit", required = true) @Min(1) @Max(MAX_LIMIT) Integer limit,
                          @RequestParam(value = "filterBy", required = false) String filterBy,
                          @RequestParam(value = "filterId", required = false) String filterId,
                          HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");

        String filename = "data-" + LocalDateTime.now() + ".csv";
        response.setHeader("Content-disposition", "attachment;filename=" + filename);

        dataService.write(getRequestMap(startTime, endTime, limit, filterBy, filterId), response.getWriter());
    }

    private HashMap<String, Object> getRequestMap(LocalDateTime startTime, LocalDateTime endTime, Integer limit, String filterBy, String filterId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("startTime", startTime.toString());
        if (endTime != null) {
            map.put("endTime", endTime.toString());
        }
        map.put("limit", limit);
        if (filterBy != null) {
            map.put("filterBy", filterBy);
            map.put("filterId", filterId);
        }
        return map;
    }
}
