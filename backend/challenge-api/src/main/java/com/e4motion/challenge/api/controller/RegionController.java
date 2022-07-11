package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.api.service.RegionService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "5. 구역 관리")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class RegionController {
    
	private final RegionService regionService;

    // TODO: 파일 일괄 등록 및 수정
    @Operation(summary = "구역 등록", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/region")
    public Response create(@Valid @RequestBody RegionDto regionDto) throws Exception {
    	
    	return new Response("region", regionService.create(regionDto));
    }

    @Operation(summary = "구역 수정", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/region/{regionNo}")
    public Response update(@PathVariable String regionNo, @RequestBody RegionDto regionDto) throws Exception {

		return new Response("region", regionService.update(regionNo, regionDto));
    }

    @Operation(summary = "구역 삭제", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/region/{regionNo}")
    public Response delete(@PathVariable String regionNo) throws Exception {
		
		regionService.delete(regionNo);
		
        return new Response();
    }

    @Operation(summary = "구역 조회", description = "접근 권한 : 최고관리자, 운영자, 데이터 사용자, 카메라 관리자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DATA', 'ROLE_CAMERA_ADMIN')")
	@GetMapping("/region/{regionNo}")
    public Response get(@PathVariable String regionNo) throws Exception {

		return new Response("region", regionService.get(regionNo));
    }

    @Operation(summary = "구역 목록 조회", description = "접근 권한 : 최고관리자, 운영자, 데이터 사용자, 카메라 관리자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DATA', 'ROLE_CAMERA_ADMIN')")
	@GetMapping("/regions")
    public Response getList() throws Exception {
		
        return new Response("regions", regionService.getList());
    }
}
