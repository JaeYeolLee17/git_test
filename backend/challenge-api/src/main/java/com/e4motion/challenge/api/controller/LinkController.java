package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.LinkDto;
import com.e4motion.challenge.api.service.LinkService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = " 6. 링크 관리")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class LinkController {
    
	private final LinkService linkService;

    @Operation(summary = "링크 등록", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/link")
    public Response create(@Valid @RequestBody LinkDto linkDto) throws Exception {
    	
    	return new Response("link", linkService.create(linkDto));
    }

    @Operation(summary = "링크 수정", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/link/{linkId}")
    public Response update(@PathVariable Long linkId, @RequestBody LinkDto linkDto) throws Exception {

		return new Response("link", linkService.update(linkId, linkDto));
    }

    @Operation(summary = "링크 삭제", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/link/{linkId}")
    public Response delete(@PathVariable Long linkId) throws Exception {
		
		linkService.delete(linkId);
		
        return new Response();
    }

    @Operation(summary = "링크 조회", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/link/{linkId}")
    public Response get(@PathVariable Long linkId) throws Exception {

		return new Response("link", linkService.get(linkId));
    }

    @Operation(summary = "링크 목록 조회", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/links")
    public Response getList() throws Exception {
		
        return new Response("links", linkService.getList());
    }
}
