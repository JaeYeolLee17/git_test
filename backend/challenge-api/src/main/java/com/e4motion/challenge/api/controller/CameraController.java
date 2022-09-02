package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.service.CameraService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = " 3. 카메라 관리")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class CameraController {
    
	private final CameraService cameraService;

    @Operation(summary = "카메라 등록", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/camera")
    public Response create(@Valid @RequestBody CameraDto cameraDto) throws Exception {
    	
    	return new Response("camera", cameraService.create(cameraDto));
    }

    @Operation(summary = "카메라 수정", description = "접근 권한 : 최고관리자, 운영자, 카메라 관리자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_CAMERA_ADMIN')")
    @PutMapping("/camera/{cameraNo}")
    public Response update(@PathVariable String cameraNo, @RequestBody CameraDto cameraDto) throws Exception {

		return new Response("camera", cameraService.update(cameraNo, cameraDto));
    }

    @Operation(summary = "카메라 삭제", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/camera/{cameraNo}")
    public Response delete(@PathVariable String cameraNo) throws Exception {
		
		cameraService.delete(cameraNo);
		
        return new Response();
    }

    @Operation(summary = "카메라 조회", description = "접근 권한 : 최고관리자, 운영자, 데이터 사용자, 카메라 관리자, 카메라")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DATA', 'ROLE_CAMERA_ADMIN', 'ROLE_CAMERA')")
	@GetMapping("/camera/{cameraNo}")
    public Response get(@PathVariable String cameraNo) throws Exception {

        // ROLE_CAMERA 의 경우 data-collector 에서 오는 요청을 처리한다.

		return new Response("camera", cameraService.get(cameraNo));
    }

    @Operation(summary = "카메라 목록 조회", description = "접근 권한 : 최고관리자, 운영자, 데이터 사용자, 카메라 관리자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DATA', 'ROLE_CAMERA_ADMIN')")
	@GetMapping("/cameras")
    public Response getList(@RequestParam(required = false) String regionNo, @RequestParam(required = false) String intersectionNo) throws Exception {
		
        return new Response("cameras", cameraService.getList(regionNo, intersectionNo));
    }
}
