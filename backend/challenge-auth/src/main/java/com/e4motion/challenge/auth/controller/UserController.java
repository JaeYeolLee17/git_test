package com.e4motion.challenge.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.auth.domain.user.User;
import com.e4motion.challenge.auth.dto.UserDto;
import com.e4motion.challenge.auth.dto.UserMapper;

@RestController
public class UserController {

	private final UserMapper mapper;
	
    @Autowired
    public UserController(UserMapper mapper) {
        this.mapper = mapper;
    }
    
	@GetMapping("/user")
	public String user() {
		User user = new User(1, 1, "userId", "name");
		UserDto userDto = mapper.toDto(user);
		return userDto.toString();
	}
	
}
