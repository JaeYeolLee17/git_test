package com.e4motion.challenge.api.service;

import java.util.List;

import com.e4motion.challenge.api.dto.UserDto;

public interface UserService {
	
    UserDto create(UserDto userDto);
    
    public UserDto update(String userId, UserDto userDto);
    
    public void delete(String userId);
    
    public UserDto get(String userId);
    
    public List<UserDto> getList();
    
}
