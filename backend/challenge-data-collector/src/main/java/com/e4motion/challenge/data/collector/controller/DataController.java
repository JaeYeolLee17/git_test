package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.data.collector.dto.DataDto;
import com.e4motion.challenge.data.collector.service.DataService;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.InvalidParamException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Tag(name = "2. Data")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v1")
public class DataController {

    private final static int MAX_LIMIT = 100000;

    private final DataService dataService;

    @PreAuthorize("hasRole('ROLE_CAMERA')")
    @PostMapping("/data")
    public Response insert(@RequestBody DataDto dataDto) {

        dataService.insert(dataDto);

        // TODO:
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //CustomUser user = (CustomUser) authentication.getPrincipal();
        //return new Response("settingsUpdated", user.isSettingsUpdated());
        return new Response();
    }

    @PreAuthorize("hasRole('ROLE_CAMERA')")
    @GetMapping("/data")
    public Response query(@RequestParam(value = "startTime", required = true) String startTime,
                          @RequestParam(value = "endTime", required = false) String endTime,
                          @RequestParam(value = "limit", required = true) Integer limit,
                          @RequestParam(value = "filterBy", required = false) String filterBy,
                          @RequestParam(value = "filterId", required = false) String filterId) {

        if (limit <= 0 || limit > MAX_LIMIT) {
            throw new InvalidParamException("Invalid limit");
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

        List<DataDto> data = dataService.query(map);
        // TODO: nextTime

        return new Response("data", data);
    }
}
