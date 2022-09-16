package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.UserCreateDto;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.security.SecurityHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = " 2. 사용자 관리")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class UserController {
    
	private final UserService userService;
    private final SecurityHelper securityHelper;

    @Operation(summary = "사용자 등록", description = "접근 권한 : 최고관리자, 운영자")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/user")
    public Response create(@Valid @RequestBody UserCreateDto userCreateDto) throws Exception {
    	
    	return new Response("user", userService.create(userCreateDto));
    }

    @Operation(summary = "사용자 수정", description = "접근 권한 : 최고관리자, 운영자, 사용자(자기 자신만)")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @PutMapping("/user/{username}")
    public Response update(@PathVariable String username, @Valid @RequestBody UserUpdateDto userUpdateDto) throws Exception {

        securityHelper.checkIfLoginUserForRoleUser(username);

		return new Response("user", userService.update(username, userUpdateDto));
    }

    @Operation(summary = "사용자 삭제", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/user/{username}")
    public Response delete(@PathVariable String username) throws Exception {
		
		userService.delete(username);
		
        return new Response();
    }

    @Operation(summary = "사용자 조회", description = "접근 권한 : 최고관리자, 운영자, 사용자(자기 자신만)")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
	@GetMapping("/user/{username}")
    public Response get(@PathVariable String username) throws Exception {

        securityHelper.checkIfLoginUserForRoleUser(username);

		return new Response("user", userService.get(username));
    }

    @Operation(summary = "사용자 목록 조회", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/users")
    public Response getList() throws Exception {
		
        return new Response("users", userService.getList());
    }
}
