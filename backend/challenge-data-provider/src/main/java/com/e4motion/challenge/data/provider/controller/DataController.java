package com.e4motion.challenge.data.provider.controller;

import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.RegExp;
import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.service.DataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.HashMap;

@Tag(name = "2. Data")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v1")
@Validated
public class DataController {

    private final static int MAX_LIMIT = 100000;

    private final DataService dataService;

    @PreAuthorize("hasRole('ROLE_DATA')")
    @GetMapping("/data")
    public Response query(@RequestParam(value = "startTime", required = true) @Pattern(regexp = RegExp.dateTime) String startTime,
                          @RequestParam(value = "endTime", required = false) @Pattern(regexp = RegExp.dateTime) String endTime,
                          @RequestParam(value = "limit", required = true) @Min(1) @Max(MAX_LIMIT) Integer limit,
                          @RequestParam(value = "filterBy", required = false) String filterBy,
                          @RequestParam(value = "filterId", required = false) String filterId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("startTime", startTime);
        if (endTime != null) {
            map.put("endTime", endTime);
        }
        map.put("limit", limit);
        if (filterBy != null) {
            map.put("filterBy", filterBy);
            map.put("filterId", filterId);
        }

        DataListDto dataList = dataService.query(map);

        Response response = new Response("data", dataList.getData());
        response.setData("nextTime", dataList.getNextTime());

        return response;
    }

}
