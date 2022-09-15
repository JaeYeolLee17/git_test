package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.security.SecurityHelper;
import com.e4motion.challenge.data.collector.service.CameraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "3. 카메라 정보")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class CameraController {

    private final CameraService cameraService;

    private final SecurityHelper securityHelper;

    @Operation(summary = "카메라 정보 조회", description = "접근 권한 : 카메라(자기 자신만)")
    @PreAuthorize("hasRole('ROLE_CAMERA')")
    @GetMapping("/camera/{cameraNo}")
    public Response get(@PathVariable String cameraNo, HttpServletRequest request) throws Exception {

        securityHelper.checkIfLoginCameraForRoleCamera(cameraNo);

        Object camera = cameraService.get(cameraNo, request.getHeader(HttpHeaders.AUTHORIZATION));
        if (camera != null) {
            cameraService.updateSettingsUpdated(cameraNo, false);
        }

        return new Response("camera", camera);
    }

}
