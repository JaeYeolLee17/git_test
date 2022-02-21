package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.data.collector.dto.DataDto;
import com.e4motion.challenge.data.collector.service.DataService;
import com.e4motion.common.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2. Data")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v1")
public class DataController {

    private final DataService dataService;

    @PreAuthorize("hasRole('ROLE_CAMERA')")
    @PostMapping("/data")
    public Response insert(@RequestBody DataDto dataDto) {

        dataService.insert(dataDto);

        return new Response();
    }
}
