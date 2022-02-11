package com.e4motion.challenge.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.api.domain.dto.UserDto;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.common.Response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "2. �����")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v1/user")
public class UserController {
    
	private final UserService userService;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/")
    public Response create(@RequestBody UserDto userDto) {
    	
    	return new Response("user", userService.create(userDto));
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{userId}")
    public Response update(@PathVariable String userId, @RequestBody UserDto userDto) {
		
		return new Response("user", userService.update(userId, userDto));
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    public Response delete(@PathVariable String userId) {
		
		userService.delete(userId);
		
        return new Response();
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_USER')")
	@GetMapping("/")
    public Response getList() {
		
        return new Response("users", userService.getList());
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_USER')")
	@GetMapping("/{userId}")
    public Response get(@PathVariable String userId) {
		
		return new Response("user", userService.get(userId));
    }
}
