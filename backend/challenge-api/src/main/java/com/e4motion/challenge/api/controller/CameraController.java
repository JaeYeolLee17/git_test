package com.e4motion.challenge.api.controller;


import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.service.CameraService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.e4motion.challenge.common.response.Response;

import javax.validation.Valid;

@Tag(name = "3. Camera")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class CameraController {

    private final CameraService cameraService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/camera")
    public Response create(@Valid @RequestBody CameraDto cameraDto) throws Exception {

        return new Response("camera", cameraService.create(cameraDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/camera/{cameraId}")
    public Response update(@PathVariable String cameraId, @RequestBody CameraDto cameraDto) throws Exception {

        return new Response("camera", cameraService.update(cameraId, cameraDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/camera/{cameraId}")
    public Response delete(@PathVariable String cameraId) throws Exception {

        cameraService.delete(cameraId);

        return new Response();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/camera/{cameraId}")
    public Response get(@PathVariable String cameraId) throws Exception {

        return new Response("camera", cameraService.get(cameraId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/cameras")
    public Response getList() throws Exception {

        return new Response("cameras", cameraService.getList());
    }
}
