package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.service.IntersectionService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "4. 교차로 관리")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class IntersectionController {
    
	private final IntersectionService intersectionService;

    // TODO: 파일 일괄 등록 및 수정
    @Operation(summary = "교차로 등록", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/intersection")
    public Response create(@Valid @RequestBody IntersectionDto intersectionDto) throws Exception {
    	
    	return new Response("intersection", intersectionService.create(intersectionDto));
    }

    @Operation(summary = "교차로 수정", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/intersection/{intersectionNo}")
    public Response update(@PathVariable String intersectionNo, @RequestBody IntersectionDto intersectionDto) throws Exception {

		return new Response("intersection", intersectionService.update(intersectionNo, intersectionDto));
    }

    @Operation(summary = "교차로 삭제", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/intersection/{intersectionNo}")
    public Response delete(@PathVariable String intersectionNo) throws Exception {
		
		intersectionService.delete(intersectionNo);
		
        return new Response();
    }

    @Operation(summary = "교차로 조회", description = "접근 권한 : 최고관리자, 운영자, 데이터 사용자, 카메라 관리자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DATA', 'ROLE_CAMERA_ADMIN')")
	@GetMapping("/intersection/{intersectionNo}")
    public Response get(@PathVariable String intersectionNo) throws Exception {

		return new Response("intersection", intersectionService.get(intersectionNo));
    }

    @Operation(summary = "교차로 목록 조회", description = "접근 권한 : 최고관리자, 운영자, 데이터 사용자, 카메라 관리자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DATA', 'ROLE_CAMERA_ADMIN')")
	@GetMapping("/intersections")
    public Response getList(@RequestParam(required = false) String regionNo) throws Exception {
		
        return new Response("intersections", intersectionService.getList(regionNo));
    }
}
