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

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.response.Response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "2. User")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v1")
public class UserController {
    
	private final UserService userService;
	
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/user")
    public Response create(@RequestBody UserDto userDto) throws Exception {
    	
    	return new Response("user", userService.create(userDto));
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/{userId}")
    public Response update(@PathVariable String userId, @RequestBody UserDto userDto) throws Exception {
		
		return new Response("user", userService.update(userId, userDto));
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/{userId}")
    public Response delete(@PathVariable String userId) throws Exception {
		
		userService.delete(userId);
		
        return new Response();
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
	@GetMapping("/user/{userId}")
    public Response get(@PathVariable String userId) throws Exception {
		
		return new Response("user", userService.get(userId));
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
	@GetMapping("/users")
    public Response getList() throws Exception {
		
        return new Response("users", userService.getList());
    }
}
