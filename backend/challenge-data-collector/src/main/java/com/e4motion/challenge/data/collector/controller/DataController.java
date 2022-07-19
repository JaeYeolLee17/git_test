package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.security.SecurityHelper;
import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.service.CameraService;
import com.e4motion.challenge.data.collector.service.DataService;
import com.e4motion.challenge.data.collector.service.DataStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "2. 교통 데이터")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class DataController {

    private final DataService dataService;
    private final DataStatsService dataStatsService;
    private final CameraService cameraService;
    private final SecurityHelper securityHelper;

    @Operation(summary = "데이터", description = "접근 권한 : 카메라(자기 자신만)")
    @PreAuthorize("hasRole('ROLE_CAMERA')")
    @PostMapping("/data")
    public Response insert(@Valid @RequestBody CameraDataDto cameraDataDto) {

        securityHelper.checkIfLoginCameraForRoleCamera(cameraDataDto.getC());

        dataService.insert(cameraDataDto);
        dataStatsService.insert(cameraDataDto);

        return new Response("settingsUpdated", cameraService.getSettingsUpdated(cameraDataDto.getC()));
    }

}
