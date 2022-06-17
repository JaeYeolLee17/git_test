package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.response.Response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Tag(name = "2. 회원 관리")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class UserController {
    
	private final UserService userService;

    @Operation(summary = "회원 추가", description = "접근 권한 : 관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/user")
    public Response create(@Valid @RequestBody UserDto userDto) throws Exception {
    	
    	return new Response("user", userService.create(userDto));
    }

    @Operation(summary = "회원 수정", description = "접근 권한 : 관리자, 운영자, 사용자(자기 자신만)")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @PutMapping("/user/{userId}")
    public Response update(@PathVariable String userId, @Valid @RequestBody UserUpdateDto userUpdateDto) throws Exception {

        // TODO: 사용자(자기 자신만) 처리.
		return new Response("user", userService.update(userId, userUpdateDto));
    }

    @Operation(summary = "회원 삭제", description = "접근 권한 : 관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/user/{userId}")
    public Response delete(@PathVariable String userId) throws Exception {
		
		userService.delete(userId);
		
        return new Response();
    }

    @Operation(summary = "회원 조회", description = "접근 권한 : 관리자, 운영자, 사용자(자기 자신만)")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
	@GetMapping("/user/{userId}")
    public Response get(@PathVariable String userId) throws Exception {

        // TODO: 사용자(자기 자신만) 처리.
		return new Response("user", userService.get(userId));
    }

    @Operation(summary = "회원 목록 조회", description = "접근 권한 : 관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/users")
    public Response getList() throws Exception {
		
        return new Response("users", userService.getList());
    }
}
