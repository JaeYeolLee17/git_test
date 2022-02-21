package com.e4motion.challenge.api.service;

import java.util.List;

import com.e4motion.challenge.api.dto.UserDto;

public interface UserService {
	
    UserDto create(UserDto userDto);
    
    UserDto update(String userId, UserDto userDto);
    
    void delete(String userId);
    
    UserDto get(String userId);
    
    List<UserDto> getList();
    
}
