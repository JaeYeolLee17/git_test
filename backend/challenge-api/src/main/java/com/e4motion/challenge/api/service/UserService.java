package com.e4motion.challenge.api.service;

import java.util.List;

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserUpdateDto;

public interface UserService {
	
    UserDto create(UserDto userDto);
    
    UserDto update(String username, UserUpdateDto userUpdateDto);
    
    void delete(String username);
    
    UserDto get(String username);
    
    List<UserDto> getList();
    
}
