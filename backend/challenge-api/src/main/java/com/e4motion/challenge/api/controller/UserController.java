package com.e4motion.challenge.api.controller;

import java.util.List;

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

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "2. »ç¿ëÀÚ")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v1/")
public class UserController {
    
	private final UserService userService;
	
    @PostMapping("signup")
    public UserDto signup(@RequestBody UserDto userDto) {
    	return userService.create(userDto);	// TODO: delete after test.
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("user")
    public UserDto create(@RequestBody UserDto userDto) {
    	return userService.create(userDto);	// TODO: return just ok after test.
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("user/{userId}")
    public String update(@PathVariable String userId) {
        return "user " + userId + " updated";
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("user/{userId}")
    public String delete(@PathVariable String userId) {
        return "user " + userId + " deleted";
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_USER')")
	@GetMapping("user")
    public List<UserDto> getList() {
        return userService.getList();
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_USER')")
	@GetMapping("user/{userId}")
    public UserDto get(@PathVariable String userId) {
		return userService.get(userId);
    }
}
