package com.e4motion.challenge.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.domain.UserRepository;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Author")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v1/")
public class UserController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("user/{id}")
    public String get(@PathVariable String id) {
        return "get user " + id;
    }
	
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("user")
    public UserDto create(@RequestBody UserDto userDto) {
    	User user =  new User(userDto.getUserId(), 
    			passwordEncoder.encode(userDto.getPassword()),
    			userDto.getUsername(),
    			userDto.getEmail(),
    			userDto.getAuthorities());
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("user/{id}")
    public String update(@PathVariable String id) {
        return "user " + id + " updated";
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("user/{id}")
    public String delete(@PathVariable String id) {
        return "user " + id + " deleted";
    }
    
}
