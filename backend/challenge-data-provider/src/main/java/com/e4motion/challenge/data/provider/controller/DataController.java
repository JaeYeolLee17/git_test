package com.e4motion.challenge.data.provider.controller;

import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.service.DataService;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.InvalidParamException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Tag(name = "2. Data")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v1")
public class DataController {

    private final static int MAX_LIMIT = 100000;

    private final DataService dataService;

    @PreAuthorize("hasRole('ROLE_DATA')")
    @GetMapping("/data")
    public Response query(@RequestParam(value = "startTime", required = true) String startTime,
                          @RequestParam(value = "endTime", required = false) String endTime,
                          @RequestParam(value = "limit", required = true) Integer limit,
                          @RequestParam(value = "filterBy", required = false) String filterBy,
                          @RequestParam(value = "filterId", required = false) String filterId) {

        // TODO: validate params
        if (limit <= 0 || limit > MAX_LIMIT) {
            throw new InvalidParamException(InvalidParamException.INVALID_LIMIT);
        }

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

        DataListDto data = dataService.query(map);

        // TODO: set nextTime

        return new Response("data", data);
    }

}
