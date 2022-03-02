package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.service.CameraService;
import com.e4motion.challenge.data.collector.service.DataService;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.InvalidParamException;
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

    private final CameraService cameraService;

    @PreAuthorize("hasRole('ROLE_CAMERA')")
    @PostMapping("/data")
    public Response insert(@RequestBody CameraDataDto cameraDataDto) {

        validateData(cameraDataDto);

        dataService.insert(cameraDataDto);

        return new Response("settingsUpdated", cameraService.getSettingsUpdated(cameraDataDto.getC()));
    }

    private void validateData(CameraDataDto cameraDataDto) {

        // TODO: validate date
        if (cameraDataDto.getV() == null) {
            throw new InvalidParamException(InvalidParamException.INVALID_DATA);
        }
    }

}
